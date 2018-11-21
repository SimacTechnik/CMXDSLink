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
        CMXDSLink.LOGGER.debug("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        makeGroupBy();
    }

    private void makeGroupBy() {
        CMXDSLink.LOGGER.debug("In makeGroupBy() method");
        Class notificationClass;
        switch(type){
            case MOVEMENT:
                CMXDSLink.LOGGER.debug("type: MOVEMENT");
                notificationClass = MovementNotification.class;
                break;
            case ASSOCIATION:
                CMXDSLink.LOGGER.debug("type: ASSOCIATION");
                notificationClass = AssociationNotification.class;
                break;
            case LOCATION_UPDATE:
                CMXDSLink.LOGGER.debug("type: LOCATION_UPDATE");
                notificationClass = LocationUpdateNotification.class;
                break;
            default:
                CMXDSLink.LOGGER.debug("In default case of switch(type)");
                CMXDSLink.LOGGER.debug("type: UNKNOWN");
                notificationClass = AssociationNotification.class;
        }
        // get all PUBLIC fields
        Field[] fields = Arrays.stream(notificationClass.getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .toArray(Field[]::new);
        Collection<Field> fieldCollection = Arrays.asList(fields);
        CMXDSLink.LOGGER.debug("All public filed names: " + String.join(", ", Arrays.stream(fields).map(Field::getName).toArray(String[]::new)));

        // get field names
        String[] fieldNames = fieldCollection.stream().map(Field::getName).toArray(String[]::new);

        // action for grouping
        Action act = new Action(Permission.READ, e -> {
            // get parameter of group by
            String selected = e.getParameter(CMXConstants.GROUP_BY).getString();
            CMXDSLink.LOGGER.debug("GROUP_BY: " + selected);
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
        CMXDSLink.LOGGER.debug("Set group by action");
    }

    public CMXNotificationManager(Node rootNode, CMXTypes type, Field groupBy) {
        CMXDSLink.LOGGER.debug("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        this.groupBy = groupBy;
        makeGroupBy();
    }

    private void render() {
        CMXDSLink.LOGGER.debug("In render() method");
        try {
            Map<String, Node> tmpData = new HashMap<>();
            for (Node node : data.values()) {
                CMXNotification notification = node.getMetaData();
                CMXDSLink.LOGGER.debug("got MetaData from deviceId: "+notification.getDeviceId());
                Node newNode;
                if (groupBy == null) {
                    CMXDSLink.LOGGER.debug("groupBy == null");
                    newNode = notification.createNode(rootNode);
                } else {
                    CMXDSLink.LOGGER.debug("groupBy == " + groupBy.getName());
                    String key = groupBy.get(node.getMetaData()).toString();
                    Node parent = getOrCreate(rootNode, key)
                            .setDisplayName(key)
                            .setSerializable(false)
                            .build();
                    newNode = notification.createNode(parent);
                }
                tmpData.put(notification.getDeviceId(), newNode);
            }
            data.values().forEach(a -> a.delete(true));
            data.clear();
            data = tmpData;
        } catch (IllegalAccessException ignore) {
            CMXDSLink.LOGGER.debug("catched IllegalAccessException in render() method");
        }
        CMXDSLink.LOGGER.debug("Leaving render() method");
    }

    private void changeGroupBy(Field groupBy) {
        if(groupBy.getName().equals(CMXConstants.DEVICE_ID))
            this.groupBy = null;
        else
            this.groupBy = groupBy;
        render();
    }

    public void reset() {
        CMXDSLink.LOGGER.debug("In reset() method");
        changeGroupBy(null);
    }

    public void update(CMXNotification notification) {
        CMXDSLink.LOGGER.debug("In update(CMXNotification notification) method");
        // create/update node
        Node parent;
        if(groupBy == null)
            parent = rootNode;
        else {
            try {
                String val = groupBy.get(notification).toString();
                parent = getOrCreate(rootNode, val)
                        .setDisplayName(val)
                        .setSerializable(false)
                        .build();
            } catch(IllegalAccessException a) {
                parent = rootNode;
            }
        }
        Node node = notification.createNode(parent);
        data.put(notification.getDeviceId(), node);
        CMXDSLink.LOGGER.debug("succesfully created nodes");
    }

    public static NodeBuilder getOrCreate(Node parent, String name) {
        CMXDSLink.LOGGER.debug("In getOrCreate(Node parent, String name) method");
        Node child = parent.getChild(name, true);
        if (child == null) {
            CMXDSLink.LOGGER.debug("child == null");
            return parent.createChild(name, true);
        } else {
            CMXDSLink.LOGGER.debug("child != null");
            return child.createFakeBuilder();
        }
    }
}
