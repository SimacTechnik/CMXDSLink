package cz.simac.cmxdslink;

import cz.simac.cmxdslink.cmxdata.CMXNotification;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CMXNotificationManager {
    private Map<String, Node> data = new HashMap<>();
    private Node rootNode;
    private Field groupBy = null;

    public CMXNotificationManager(Node rootNode){
        this.rootNode = rootNode;
    }

    public CMXNotificationManager(Node rootNode, Field groupBy) {
        this.rootNode = rootNode;
        this.groupBy = groupBy;
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

    public void changeGroupBy(Field groupBy) {
        this.groupBy = groupBy;
        render();
    }

    public void reset() {
        changeGroupBy(null);
    }

    public void update(CMXNotification notification) {
        if(data.containsKey(notification.getDeviceId())) {
            Node n = data.get(notification.getDeviceId());
            n.clearChildren();
            n.addChildren(new ArrayList<>(notification.createNode().getChildren().values()));
            n.setMetaData(notification);
            return;
        }
        data.put(notification.getDeviceId(), notification.createNode());
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
