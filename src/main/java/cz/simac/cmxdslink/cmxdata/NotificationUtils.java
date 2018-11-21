package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonArray;

import java.util.Arrays;

public class NotificationUtils {
    public static void createNode(Node parent, String name, String value) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, Number value) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, Boolean value) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, String[] value) {
        if(value == null) {
            CMXDSLink.LOGGER.debug(name + " == null");
            return;
        }
        JsonArray tmp = new JsonArray();
        tmp.addAll(0, Arrays.asList(value));
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.ARRAY)
                .setValue(new Value(tmp))
                .build();
    }
}
