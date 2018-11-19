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
        CMXDSLink.LOGGER.trace("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        makeGroupBy();
    }

    private void makeGroupBy() {
        CMXDSLink.LOGGER.trace("In makeGroupBy() method");
        Class notificationClass;
        switch(type){
            case MOVEMENT:
                CMXDSLink.LOGGER.trace("type: MOVEMENT");
                notificationClass = MovementNotification.class;
                break;
            case ASSOCIATION:
                CMXDSLink.LOGGER.trace("type: ASSOCIATION");
                notificationClass = AssociationNotification.class;
                break;
            case LOCATION_UPDATE:
                CMXDSLink.LOGGER.trace("type: LOCATION_UPDATE");
                notificationClass = LocationUpdateNotification.class;
                break;
            default:
                CMXDSLink.LOGGER.trace("In default case of switch(type)");
                CMXDSLink.LOGGER.trace("type: UNKNOWN");
                notificationClass = AssociationNotification.class;
        }
        // get all PUBLIC fields
        Field[] fields = Arrays.stream(notificationClass.getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .toArray(Field[]::new);
        Collection<Field> fieldCollection = Arrays.asList(fields);
        CMXDSLink.LOGGER.trace("All public filed names: " + String.join(", ", Arrays.stream(fields).map(Field::getName).toArray(String[]::new)));

        // get field names
        String[] fieldNames = fieldCollection.stream().map(Field::getName).toArray(String[]::new);

        // action for grouping
        Action act = new Action(Permission.READ, e -> {
            // get parameter of group by
            String selected = e.getParameter(CMXConstants.GROUP_BY).getString();
            CMXDSLink.LOGGER.trace("GROUP_BY: " + selected);
            // get field which have this name
            changeGroupBy(fieldCollection.stream().filter(a -> a.getName().equals(selected)).findFirst().get());
            CMXDSLink.LOGGER.debug("grouping by: " + selected);
        });
        // create ValueType for Group By Parameter
        ValueType groupByEnum = ValueType.makeEnum(fieldNames);

        // add parameter group by
        act.addParameter(new Parameter(CMXConstants.GROUP_BY, groupByEnum, new Value(groupByEnum.getEnums().toArray(new String[0])[0])));

        // setting the action
        Node anode = rootNode.getChild(CMXConstants.GROUP_BY, true);
        if(anode == null) rootNode.createChild(CMXConstants.GROUP_BY, true).setAction(act).build().setSerializable(false);
        else anode.setAction(act);
        CMXDSLink.LOGGER.trace("Set group by action");
    }

    public CMXNotificationManager(Node rootNode, CMXTypes type, Field groupBy) {
        CMXDSLink.LOGGER.trace("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        this.groupBy = groupBy;
        makeGroupBy();
    }

    private void render() {
        CMXDSLink.LOGGER.trace("In render() method");
        rootNode.clearChildren();
        try {
            for (Node node : data.values()) {
                if (groupBy == null) {
                    CMXDSLink.LOGGER.trace("groupBy == null");
                    rootNode.addChild(node);
                } else {
                    CMXDSLink.LOGGER.trace("groupBy == " + groupBy.getName());
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
        if(groupBy.getName().equals(CMXConstants.DEVICE_ID))
            this.groupBy = null;
        else
            this.groupBy = groupBy;
        render();
    }

    public void reset() {
        CMXDSLink.LOGGER.trace("In reset() method");
        changeGroupBy(null);
    }

    public void update(CMXNotification notification) {
        CMXDSLink.LOGGER.trace("In update(CMXNotification notification) method");
        // update old node
        if(data.containsKey(notification.getDeviceId())) {
            CMXDSLink.LOGGER.trace("updating nodes");
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
        CMXDSLink.LOGGER.trace("creating nodes");
        // create new node
        Node node = notification.createNode();
        rootNode.addChild(node);
        data.put(notification.getDeviceId(), node);
    }

    private static NodeBuilder getOrCreate(Node parent, String name) {
        CMXDSLink.LOGGER.trace("In getOrCreate(Node parent, String name) method");
        Node child = parent.getChild(name, true);
        if (child == null) {
            CMXDSLink.LOGGER.trace("child == null");
            return parent.createChild(name, true);
        } else {
            CMXDSLink.LOGGER.trace("child != null");
            return child.createFakeBuilder();
        }
    }
}
