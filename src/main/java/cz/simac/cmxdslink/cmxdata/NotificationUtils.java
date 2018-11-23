package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.link.Linkable;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonArray;

import java.util.Arrays;

public class NotificationUtils {
    public static Linkable link = null;

    public static Node createNode(Node parent, String name, String value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return null;
        }
        return hideNode(CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .build(), visible);
    }

    public static Node createNode(Node parent, String name, Number value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return null;
        }
        return hideNode(CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .build(), visible);
    }

    public static Node createNode(Node parent, String name, Boolean value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return null;
        }
        return hideNode(CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .build(), visible);
    }

    public static Node createNode(Node parent, String name, String[] value, Boolean visible) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return null;
        }
        JsonArray tmp = new JsonArray();
        tmp.addAll(0, Arrays.asList(value));
        return hideNode(CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.ARRAY)
                .setValue(new Value(tmp))
                .build(), visible);
    }

    private static Node hideNode(Node n, Boolean visible) {
        if(!visible) {
            n.delete(false);
            return null;
        }
        return n;
    }


    public static void setAction(Node n, String name, Action filterAction) {
        if(n == null)
            return;
        CMXNotificationManager.getOrCreate(n, name)
                .setAction(filterAction)
                .build()
                .setSerializable(false);
    }
}
