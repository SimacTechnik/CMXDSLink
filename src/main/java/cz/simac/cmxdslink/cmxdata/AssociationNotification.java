package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.util.json.JsonArray;
import org.dsa.iot.dslink.util.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Node createNode(Node parent, Map<String, Boolean> filter) {
        CMXDSLink.LOGGER.debug("In AssociationNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "deviceId", deviceId, filter.get("deviceId"));
        NotificationUtils.createNode(n, "entity", entity, filter.get("entity"));
        NotificationUtils.createNode(n, "notificationType", notificationType, filter.get("notificationType"));
        NotificationUtils.createNode(n, "band", band, filter.get("band"));
        NotificationUtils.createNode(n, "ssid", ssid, filter.get("ssid"));
        NotificationUtils.createNode(n, "ipAddress", ipAddress, filter.get("ipAddress"));
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName, filter.get("subscriptionName"));
        NotificationUtils.createNode(n, "timestamp", timestamp, filter.get("timestamp"));
        NotificationUtils.createNode(n, "status", status, filter.get("status"));
        NotificationUtils.createNode(n, "username", username, filter.get("username"));
        NotificationUtils.createNode(n, "lastSeen", lastSeen, filter.get("lastSeen"));
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress, filter.get("apMacAddress"));
        NotificationUtils.createNode(n, "eventId", eventId, filter.get("eventId"));
        NotificationUtils.createNode(n, "association", association, filter.get("association"));
        n.setMetaData(this);
        return n;
    }
}
