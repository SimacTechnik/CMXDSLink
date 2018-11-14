package cz.simac.cmxdslink;

import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CMXNotify {
    public String deviceId;
    public String username;
    public Boolean associated;
    public String locationMapHierarchy;
    public Number timestamp;
    public String manufacturer;
    public String apMacAddress;
    public String band;

    public CMXNotify() {

    }

    //copy constructor
    public CMXNotify(CMXNotify notify) {
        this.deviceId = notify.deviceId;
        this.username = notify.username;
        this.associated = notify.associated;
        this.locationMapHierarchy = notify.locationMapHierarchy;
        this.timestamp = notify.timestamp;
        this.manufacturer = notify.manufacturer;
        this.apMacAddress = notify.apMacAddress;
        this.band = notify.band;
    }

    // encode string input to CMXNotify object
    public static CMXNotify[] encodeJSON(String data) {
        JSONParser parser = new JSONParser();
        List<CMXNotify> list = new ArrayList<>();
        try {
            final JSONObject jsonObject = (JSONObject) parser.parse(data);
            //CMX notifications are always in array with key "notifications"
            final JSONArray jsonArray = (JSONArray) jsonObject.get("notifications");
            //if JSON was correctly parsed, but does not contains key 'notifications'
            if (jsonArray == null)
                return null;
            for (Object obj : jsonArray) {
                JSONObject jObj = (JSONObject) obj;
                list.add(new CMXNotify() {{
                    deviceId = (String) jObj.get("deviceId");
                    username = (String) jObj.get("username");
                    associated = (Boolean) jObj.get("associated");
                    locationMapHierarchy = (String) jObj.get("locationMapHierarchy");
                    timestamp = (Number) jObj.get("timestamp");
                    manufacturer = (String) jObj.get("manufacturer");
                    apMacAddress = (String) jObj.get("apMacAddress");
                    band = (String) jObj.get("band");
                }});
            }
        } catch (ParseException pe) {
            return null;
        }

        return list.toArray(new CMXNotify[list.size()]);
    }

    public void update(Node rootNode) {
        // System.out.println("in update() method");
        Node node = getOrCreate(rootNode, deviceId)
                .setDisplayName(deviceId)
                .setSerializable(true)
                .build();
        createNode(node);
    }

    public void createNode(Node node) {
        // System.out.println("in createNode() method");
        createNodeChild(node, "username", this.username);
        createNodeChild(node, "associated", this.associated);
        createNodeChild(node, "locationMapHierarchy", this.locationMapHierarchy);
        createNodeChild(node, "timestamp", this.timestamp);
        createNodeChild(node, "manufacturer", this.manufacturer);
        createNodeChild(node, "apMacAddress", this.apMacAddress);
        createNodeChild(node, "band", this.band);
    }

    private void createNodeChild(Node node, String name, String value) {
        // System.out.println("in createNodeChild for string value: " + value + " method");
        if (value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(true)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .build();
        // System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getString() + " with parent name: " + n.getParent().getDisplayName());
    }

    private void createNodeChild(Node node, String name, Number value) {
        // System.out.println("in createNodeChild for number value: "+value.toString()+" method");
        if (value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .build();
        // System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getNumber() + " with parent name: " + n.getParent().getDisplayName());
    }

    private void createNodeChild(Node node, String name, Boolean value) {
        // System.out.println("in createNodeChild for boolean value: "+value.toString()+" method");
        if (value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .build();
        // System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getBool() + " with parent name: " + n.getParent().getDisplayName());
    }

    @Override
    public String toString() {
        return "[CMXNotify object] {" +
                "deviceId: " + deviceId + ", " +
                "username: " + username + ", " +
                "associated: " + associated.toString() + ", " +
                "locationMapHierarchy: " + locationMapHierarchy + ", " +
                "timestamp: " + timestamp.toString() + ", " +
                "manufacturer: " + manufacturer + ", " +
                "apMacAddress: " + apMacAddress + ", " +
                "band: " + band + '}';
    }

    private NodeBuilder getOrCreate(Node node, String name) {
        // System.out.println("in getOrCreate() method");
        Node child = node.getChild(name, true);
        if (child == null) {
            // System.out.println("creating new child");
            return node.createChild(name, true);
        } else {
            // System.out.println("creating fake builder");
            return child.createFakeBuilder();
        }
    }
}