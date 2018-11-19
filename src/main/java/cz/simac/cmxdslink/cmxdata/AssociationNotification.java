package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.util.json.JsonArray;
import org.dsa.iot.dslink.util.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AssociationNotification implements CMXNotification {
    public String entity;
    public String notificationType;
    public String band;
    public String ssid;
    public String[] ipAddress;
    public String subscriptionName;
    public Number timestamp;
    public Number status;
    public String username;
    public String lastSeen;
    public String apMacAddress;
    public Number eventId;
    public Boolean association;
    public String deviceId;

    @Override
    public CMXTypes getType() {
        return CMXTypes.ASSOCIATION;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Node createNode() {
        CMXDSLink.LOGGER.debug("In AssociationNotification::createNode() method");
        Node n = new Node(getDeviceId(), null, null, true);
        n.setDisplayName(getDeviceId());
        NotificationUtils.createNode(n, "deviceId", deviceId);
        NotificationUtils.createNode(n, "entity", entity);
        NotificationUtils.createNode(n, "notificationType", notificationType);
        NotificationUtils.createNode(n, "band", band);
        NotificationUtils.createNode(n, "ssid", ssid);
        NotificationUtils.createNode(n, "ipAddress", ipAddress);
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName);
        NotificationUtils.createNode(n, "timestamp", timestamp);
        NotificationUtils.createNode(n, "status", status);
        NotificationUtils.createNode(n, "username", username);
        NotificationUtils.createNode(n, "lastSeen", lastSeen);
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress);
        NotificationUtils.createNode(n, "eventId", eventId);
        NotificationUtils.createNode(n, "association", association);
        n.setMetaData(this);
        n.setSerializable(false);
        return n;
    }
}
