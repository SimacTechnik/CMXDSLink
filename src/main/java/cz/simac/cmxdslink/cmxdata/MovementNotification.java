package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXConstants;
import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.actions.Action;
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
    public Node createNode(Node parent, Map<String, Boolean> filter, Action filterAction) {
        CMXDSLink.LOGGER.debug("In MovementNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.setAction(NotificationUtils.createNode(n, "deviceId", deviceId, filter.get("deviceId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor, filter.get("confidenceFactor")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "floorId", floorId, filter.get("floorId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "lastSeen", lastSeen, filter.get("lastSeen")),
                CMXConstants.FILTER_OUT, filterAction);
        if(geoGeoCoordinate != null)
            geoGeoCoordinate.createNode(n, "geoGeoCoordinate");
        NotificationUtils.setAction(NotificationUtils.createNode(n, "associated", associated, filter.get("associated")),
                CMXConstants.FILTER_OUT, filterAction);
        if(locationCoordinate != null)
            locationCoordinate.createNode(n, "locationCoordinate");
        NotificationUtils.setAction(NotificationUtils.createNode(n, "eventId", eventId, filter.get("eventId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "notificationType", notificationType, filter.get("notificationType")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "moveDistanceInFt", moveDistanceInFt, filter.get("moveDistanceInFt")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "timestamp", timestamp, filter.get("timestamp")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "apMacAddress", apMacAddress, filter.get("apMacAddress")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "band", band, filter.get("band")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "floorRefId", floorRefId, filter.get("floorRefId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "ssid", ssid, filter.get("ssid")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "username", username, filter.get("username")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "subscriptionName", subscriptionName, filter.get("subscriptionName")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy, filter.get("locationMapHierarchy")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "ipAddress", ipAddress, filter.get("ipAddress")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "entity", entity, filter.get("entity")),
                CMXConstants.FILTER_OUT, filterAction);
        n.setMetaData(this);
        return n;
    }
}
