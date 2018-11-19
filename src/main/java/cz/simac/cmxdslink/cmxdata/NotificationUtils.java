package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonArray;

import java.util.Arrays;

public class NotificationUtils {
    public static void createNode(Node parent, String name, String value) {
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, Number value) {
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, Boolean value) {
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .build();
    }

    public static void createNode(Node parent, String name, String[] value) {
        JsonArray tmp = new JsonArray();
        tmp.addAll(0, Arrays.asList(value));
        parent.createChild(name, true)
                .setDisplayName(name)
                .setValueType(ValueType.ARRAY)
                .setValue(new Value(tmp))
                .build();
    }
}
