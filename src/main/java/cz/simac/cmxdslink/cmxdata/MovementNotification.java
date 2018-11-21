package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.util.NodeUtils;

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
    public Node createNode(Node parent) {
        CMXDSLink.LOGGER.debug("In MovementNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "deviceId", deviceId);
        NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor);
        NotificationUtils.createNode(n, "floorId", floorId);
        NotificationUtils.createNode(n, "lastSeen", lastSeen);
        if(geoGeoCoordinate != null)
            geoGeoCoordinate.createNode(n, "geoGeoCoordinate");
        NotificationUtils.createNode(n, "associated", associated);
        if(locationCoordinate != null)
            locationCoordinate.createNode(n, "locationCoordinate");
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
