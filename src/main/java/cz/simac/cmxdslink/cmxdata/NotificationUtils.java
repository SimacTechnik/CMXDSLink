package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.link.Linkable;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonArray;

import java.util.Arrays;

public class NotificationUtils {
    public static Linkable link = null;

    public static void createNode(Node parent, String name, String value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .setHidden(!visible)
                .build();
    }

    public static void createNode(Node parent, String name, Number value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .setHidden(!visible)
                .build();
    }

    public static void createNode(Node parent, String name, Boolean value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .setHidden(!visible)
                .build();
    }

    public static void createNode(Node parent, String name, String[] value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        JsonArray tmp = new JsonArray();
        tmp.addAll(0, Arrays.asList(value));
        CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.ARRAY)
                .setValue(new Value(tmp))
                .setHidden(!visible)
                .build();
    }

    public static Node addChild(Node parent, Node child){
        return new NodeBuilder(parent, child).build();
    }

    public static Node copyNode(Node parent, Node n) {
        return CMXNotificationManager.getOrCreate(parent, n.getName())
                .setDisplayName(n.getDisplayName())
                .setSerializable(n.isSerializable())
                .setValueType(n.getValueType())
                .setValue(n.getValue())
                .setHasChildren(n.getHasChildren())
                .build();
    }
}
