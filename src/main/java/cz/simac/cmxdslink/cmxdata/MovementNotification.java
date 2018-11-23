package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.util.NodeUtils;

import java.util.Map;

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
    public Node createNode(Node parent, Map<String, Boolean> filter) {
        CMXDSLink.LOGGER.debug("In MovementNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "deviceId", deviceId, filter.get("deviceId"));
        NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor, filter.get("confidenceFactor"));
        NotificationUtils.createNode(n, "floorId", floorId, filter.get("floorId"));
        NotificationUtils.createNode(n, "lastSeen", lastSeen, filter.get("lastSeen"));
        if(geoGeoCoordinate != null)
            geoGeoCoordinate.createNode(n, "geoGeoCoordinate");
        NotificationUtils.createNode(n, "associated", associated, filter.get("associated"));
        if(locationCoordinate != null)
            locationCoordinate.createNode(n, "locationCoordinate");
        NotificationUtils.createNode(n, "eventId", eventId, filter.get("eventId"));
        NotificationUtils.createNode(n, "notificationType", notificationType, filter.get("notificationType"));
        NotificationUtils.createNode(n, "moveDistanceInFt", moveDistanceInFt, filter.get("moveDistanceInFt"));
        NotificationUtils.createNode(n, "timestamp", timestamp, filter.get("timestamp"));
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress, filter.get("apMacAddress"));
        NotificationUtils.createNode(n, "band", band, filter.get("band"));
        NotificationUtils.createNode(n, "floorRefId", floorRefId, filter.get("floorRefId"));
        NotificationUtils.createNode(n, "ssid", ssid, filter.get("ssid"));
        NotificationUtils.createNode(n, "username", username, filter.get("username"));
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName, filter.get("subscriptionName"));
        NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy, filter.get("locationMapHierarchy"));
        NotificationUtils.createNode(n, "ipAddress", ipAddress, filter.get("ipAddress"));
        NotificationUtils.createNode(n, "entity", entity, filter.get("entity"));
        n.setMetaData(this);
        return n;
    }
}
