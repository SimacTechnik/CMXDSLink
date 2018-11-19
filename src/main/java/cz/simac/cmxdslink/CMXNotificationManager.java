package cz.simac.cmxdslink;

import cz.simac.cmxdslink.cmxdata.*;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CMXNotificationManager {
    private Map<String, Node> data = new HashMap<>();
    private Node rootNode;
    private Field groupBy = null;
    private CMXTypes type;

    public CMXNotificationManager(Node rootNode, CMXTypes type){
        this.rootNode = rootNode;
        this.type = type;
        makeGroupBy();
    }

    private void makeGroupBy() {
        Class notificationClass;
        switch(type){
            case MOVEMENT:
                notificationClass = MovementNotification.class;
                break;
            case ASSOCIATION:
                notificationClass = AssociationNotification.class;
                break;
            case LOCATION_UPDATE:
                notificationClass = LocationUpdateNotification.class;
                break;
            default:
                notificationClass = AssociationNotification.class;
        }
        // get all PUBLIC fields
        Field[] fields = Arrays.asList(notificationClass.getDeclaredFields()).stream()
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .toArray(Field[]::new);
        Collection<Field> fieldCollection = Arrays.asList(fields);

        // get field names
        String[] fieldNames = fieldCollection.stream().map(a -> a.getName()).toArray(String[]::new);

        // action for grouping
        Action act = new Action(Permission.READ, e -> {
            // get parameter of group by
            String selected = e.getParameter(CMXConstants.GROUP_BY).getString();
            // get field which have this name
            changeGroupBy(fieldCollection.stream().filter(a -> a.getName() == selected).findFirst().get());
        });
        // create ValueType for Group By Parameter
        ValueType groupByEnum = ValueType.makeEnum(fieldNames);

        // add parameter group by
        act.addParameter(new Parameter(CMXConstants.GROUP_BY, groupByEnum, new Value(groupByEnum.getEnums().toArray(new String[0])[0])));

        // setting the action
        Node anode = rootNode.getChild(CMXConstants.GROUP_BY, true);
        if(anode == null) rootNode.createChild(CMXConstants.GROUP_BY, true).setAction(act).build().setSerializable(false);
        else anode.setAction(act);
    }

    public CMXNotificationManager(Node rootNode, CMXTypes type, Field groupBy) {
        this.rootNode = rootNode;
        this.type = type;
        this.groupBy = groupBy;
        makeGroupBy();
    }

    private void render() {
        rootNode.clearChildren();
        try {
            for (Node node : data.values()) {
                if (groupBy == null) {
                    rootNode.addChild(node);
                } else {
                    String key = groupBy.get(node).toString();
                    getOrCreate(rootNode, key)
                            .setDisplayName(key)
                            .build()
                            .addChild(node);
                }
            }
        } catch (IllegalAccessException ignore) {}
    }

    private void changeGroupBy(Field groupBy) {
        this.groupBy = groupBy;
        render();
    }

    public void reset() {
        changeGroupBy(null);
    }

    public void update(CMXNotification notification) {
        // update old node
        if(data.containsKey(notification.getDeviceId())) {
            Node n = data.get(notification.getDeviceId());
            // update every node in existing parent node
            for(Node node : notification.createNode().getChildren().values()){
                getOrCreate(n, node.getName())
                        .setDisplayName(n.getDisplayName())
                        .setValueType(n.getValueType())
                        .setValue(n.getValue())
                        .setSerializable(false)
                        .build();
            }
            n.setMetaData(notification);
            return;
        }
        // create new node
        Node node = notification.createNode();
        rootNode.addChild(node);
        data.put(notification.getDeviceId(), node);
    }

    private static NodeBuilder getOrCreate(Node parent, String name) {
        Node child = parent.getChild(name, true);
        if (child == null) {
            return parent.createChild(name, true);
        } else {
            return child.createFakeBuilder();
        }
    }
}
