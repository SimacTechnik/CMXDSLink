package cz.simac.cmxdslink;

import com.fasterxml.jackson.core.JsonParseException;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMXNotify
{
    public CMXNotify(){

    }

    public CMXNotify(CMXNotify notify){
        this.deviceId = notify.deviceId;
        this.username = notify.username;
        this.associated = notify.associated;
        this.locationMapHierarchy = notify.locationMapHierarchy;
        this.timestamp = notify.timestamp;
        this.manufacturer = notify.manufacturer;
        this.apMacAddress = notify.apMacAddress;
        this.band = notify.band;
    }

    public String deviceId;
    public String username;
    public Boolean associated;
    public String locationMapHierarchy;
    public Number timestamp;
    public String manufacturer;
    public String apMacAddress;
    public String band;


    public static CMXNotify[] encodeJSON(String data){
        JSONParser parser = new JSONParser();
        List<CMXNotify> list = new ArrayList<>();
        try {
            final JSONObject jsonObject = (JSONObject)parser.parse(data);
            final JSONArray jsonArray = (JSONArray)jsonObject.get("notifications");
            for(Object obj : jsonArray){
                JSONObject jObj = (JSONObject) obj;
                list.add(new CMXNotify()
                {{
                    deviceId = (String)jObj.get("deviceId");
                    username = (String)jObj.get("username");
                    associated = (Boolean) jObj.get("associated");
                    locationMapHierarchy = (String)jObj.get("locationMapHierarchy");
                    timestamp = (Number) jObj.get("timestamp");
                    manufacturer = (String)jObj.get("manufacturer");
                    apMacAddress = (String)jObj.get("apMacAddress");
                    band = (String)jObj.get("band");
                }});
            }
        }
        catch(ParseException pe){
            return null;
        }
        CMXNotify[] outArray = new CMXNotify[list.size()];
        list.toArray(outArray);
        return outArray;
    }

    public void update(Node rootNode){
        System.out.println("in update() method");
        Node node = getOrCreate(rootNode, deviceId.replace(":", ""))
                .setDisplayName(deviceId)
                .setSerializable(true)
                .build();
        createNode(node);
    }

    public void createNode(Node node) {
        System.out.println("in createNode() method");
        createNodeChild(node, "username", this.username);
        createNodeChild(node, "associated", this.associated);
        createNodeChild(node, "locationMapHierarchy", this.locationMapHierarchy);
        createNodeChild(node, "timestamp", this.timestamp);
        createNodeChild(node, "manufacturer", this.manufacturer);
        createNodeChild(node, "apMacAddress", this.apMacAddress);
        createNodeChild(node, "band", this.band);
    }

    private void createNodeChild(Node node, String name, String value) {
        System.out.println("in createNodeChild for string value: " + value + " method");
        if(value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(true)
                .setValueType(ValueType.STRING)
                .setValue(new Value(value))
                .build();
        System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getString() + " with parent name: " + n.getParent().getDisplayName());
    }

    private void createNodeChild(Node node, String name, Number value) {
        System.out.println("in createNodeChild for number value: "+value.toString()+" method");
        if(value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(value))
                .build();
        System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getNumber() + " with parent name: " + n.getParent().getDisplayName());
    }

    private void createNodeChild(Node node, String name, Boolean value) {
        System.out.println("in createNodeChild for boolean value: "+value.toString()+" method");
        if(value == null) return;
        Node n = getOrCreate(node, name)
                .setDisplayName(name)
                .setSerializable(false)
                .setValueType(ValueType.BOOL)
                .setValue(new Value(value))
                .build();
        System.out.println("created node with name: " + n.getDisplayName() + " and value: " + n.getValue().getBool() + " with parent name: " + n.getParent().getDisplayName());
    }

    @Override
    public String toString(){
        return "CMXNotify object: \n" +
                "deviceId: " + deviceId + '\n' +
                "username: " + username + '\n' +
                "associated: " + associated == null ? "null" :associated.toString() + '\n' +
                "locationMapHierarchy: " + locationMapHierarchy + '\n' +
                "timestamp: " + timestamp == null ? "null" : timestamp.toString() + '\n' +
                "manufacturer: " + manufacturer + '\n' +
                "apMacAddress: " + apMacAddress + '\n' +
                "band: " + band;
    }

    private NodeBuilder getOrCreate(Node node, String name){
        System.out.println("in getOrCreate() method");
        Node child = node.getChild(name, false);
        if(child == null) {
            System.out.println("creating new child");
            return node.createChild(name, false);
        }
        else {
            System.out.println("creating fake builder");
            return child.createFakeBuilder();
        }
    }
}
