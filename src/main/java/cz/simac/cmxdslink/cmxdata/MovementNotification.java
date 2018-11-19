package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public class MovementNotification implements CMXNotification {
    public Number confidenceFactor;
    public Number floorId;
    public String lastSeen;
    public GeoCoordinate geoGeoCoordinate;
    public Boolean associated;
    public LocationCoordinate locationCoordinate;
    public Number eventId;
    public String notificationType;
    public Number moveDistanceInFt;
    public Number timestamp;
    public String apMacAddress;
    public String band;
    public String floorRefId;
    public String deviceId;
    public String ssid;
    public String username;
    public String subscriptionName;
    public String locationMapHierarchy;
    public String[] ipAddress;
    public String entity;

    @Override
    public CMXTypes getType() {
        return CMXTypes.MOVEMENT;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Node createNode() {
        Node n = new Node(getDeviceId(), null, null, true);
        n.setDisplayName(getDeviceId());
        NotificationUtils.createNode(n, "deviceId", deviceId);
        NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor);
        NotificationUtils.createNode(n, "floorId", floorId);
        NotificationUtils.createNode(n, "lastSeen", lastSeen);
        n.addChild(geoGeoCoordinate.createNode("geoGeoCoordinate"));
        NotificationUtils.createNode(n, "associated", associated);
        n.addChild(locationCoordinate.createNode("locationCoordinate"));
        NotificationUtils.createNode(n, "eventId", eventId);
        NotificationUtils.createNode(n, "notificationType", notificationType);
        NotificationUtils.createNode(n, "moveDistanceInFt", moveDistanceInFt);
        NotificationUtils.createNode(n, "timestamp", timestamp);
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress);
        NotificationUtils.createNode(n, "band", band);
        NotificationUtils.createNode(n, "floorRefId", floorRefId);
        NotificationUtils.createNode(n, "ssid", ssid);
        NotificationUtils.createNode(n, "username", username);
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName);
        NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy);
        NotificationUtils.createNode(n, "ipAddress", ipAddress);
        NotificationUtils.createNode(n, "entity", entity);
        n.setMetaData(this);
        return n;
    }
}
