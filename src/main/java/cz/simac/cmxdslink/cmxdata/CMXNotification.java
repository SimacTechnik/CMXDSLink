package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

import java.util.Map;

public interface CMXNotification {
    CMXTypes getType();
    String getDeviceId();
    Node createNode(Node parent, Map<String, Boolean> filter);
}
