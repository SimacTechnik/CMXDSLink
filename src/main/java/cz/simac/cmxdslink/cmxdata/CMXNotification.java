package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public interface CMXNotification {
    CMXTypes getType();
    String getDeviceId();
    Node createNode(Node parent);
}
