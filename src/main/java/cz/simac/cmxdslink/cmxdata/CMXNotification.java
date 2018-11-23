package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.actions.Action;

import java.util.Map;

public interface CMXNotification {
    CMXTypes getType();
    String getDeviceId();
    Node createNode(Node parent, Map<String, Boolean> filter, Action filterAction);
}
