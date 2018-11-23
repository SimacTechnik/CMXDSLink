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
    private Map<String, CMXNotification> data = new HashMap<>();
    private Node rootNode;
    private Class notificationClass;
    private Field groupBy = null;
    private Map<String, Boolean> filter = new HashMap<>();
    private Action filterAction;
    private CMXTypes type;

    public CMXNotificationManager(Node rootNode, CMXTypes type){
        CMXDSLink.LOGGER.debug("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        makeNotificationClass();
        makeGroupBy();
        makeFiltering();
    }

    public CMXNotificationManager(Node rootNode, CMXTypes type, Field groupBy) {
        CMXDSLink.LOGGER.debug("In CMXNotificationManager ctor");
        this.rootNode = rootNode;
        this.type = type;
        this.groupBy = groupBy;
        makeNotificationClass();
        makeGroupBy();
        makeFiltering();
    }

    private void makeNotificationClass() {
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
    }

    private void makeFiltering() {
        CMXDSLink.LOGGER.debug("In makeFilter() method");
        // get all PUBLIC fields
        Field[] fields = Arrays.stream(notificationClass.getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .toArray(Field[]::new);
        Collection<Field> fieldCollection = Arrays.asList(fields);

        // get field names
        String[] fieldNames = fieldCollection.stream().map(Field::getName).toArray(String[]::new);

        // default values (true)
        for(String fieldName: fieldNames) {
            filter.put(fieldName, true);
        }

        // action for reset filter
        Action act = new Action(Permission.READ, e -> {
            synchronized (filter) {
                for (String fieldName : fieldNames) {
                    filter.put(fieldName, true);
                }
            }
           render();
        });

        // action for every node
        filterAction = new Action(Permission.READ, e -> {
            synchronized (filter) {
                filter.put(e.getNode().getParent().getName(), false);
            }
            render();
        });

        // setting the action
        Node anode = rootNode.getChild(CMXConstants.RESET_FILTER, true);
        if(anode == null) rootNode.createChild(CMXConstants.RESET_FILTER, true).setAction(act).build().setSerializable(false);
        else anode.setAction(act);
    }

    private void makeGroupBy() {
        CMXDSLink.LOGGER.debug("In makeGroupBy() method");
        // get all PUBLIC fields
        Field[] fields = Arrays.stream(notificationClass.getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .toArray(Field[]::new);
        Collection<Field> fieldCollection = Arrays.asList(fields);

        // get field names
        String[] fieldNames = fieldCollection.stream().map(Field::getName).toArray(String[]::new);

        // action for grouping
        Action act = new Action(Permission.READ, e -> {
            // get parameter of group by
            String selected = e.getParameter(CMXConstants.GROUP_BY).getString();
            CMXDSLink.LOGGER.debug("GROUP_BY: " + selected);
            // get field which have this name
            changeGroupBy(fieldCollection.stream().filter(a -> a.getName().equals(selected)).findFirst().get());
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

    private void render() {
        CMXDSLink.LOGGER.debug("In render() method");
        try {
            if(rootNode.getChildren() != null){
                for(Node n : rootNode.getChildren().values()) {
                    if(n.getAction() != null) {
                        continue;
                    }
                    rootNode.removeChild(n, false);
                }
            }
            synchronized (data) {
                for (CMXNotification notification : data.values()) {
                    if (groupBy == null) {
                        synchronized (filter) {
                            notification.createNode(rootNode, filter, filterAction);
                        }
                    } else {
                        String key = groupBy.get(notification).toString();
                        Node parent = getOrCreate(rootNode, key)
                                .setDisplayName(key)
                                .setSerializable(false)
                                .build();
                        synchronized (filter) {
                            notification.createNode(parent, filter, filterAction);
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignore) {
            CMXDSLink.LOGGER.debug("catched IllegalAccessException in render() method");
        }
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
        synchronized (filter) {
            notification.createNode(parent, filter, filterAction);
        }
        synchronized (data) {
            data.put(notification.getDeviceId(), notification);
        }
        CMXDSLink.LOGGER.debug("succesfully created nodes");
    }

    public static NodeBuilder getOrCreate(Node parent, String name) {
        CMXDSLink.LOGGER.debug("In getOrCreate(Node parent, String name) method");
        Node child = parent.getChild(name, true);
        if (child == null) {
            return parent.createChild(name, true);
        } else {
            return child.createFakeBuilder();
        }
    }
}
