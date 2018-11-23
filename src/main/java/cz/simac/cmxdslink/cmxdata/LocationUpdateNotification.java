package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.node.Node;

import java.util.Map;

public class LocationUpdateNotification implements CMXNotification {
    public String entity;
    public String band;
    public GeoCoordinate geoCoordinate;
    public String notificationType;
    public String[] ipAddress;
    public String bleTagInfo;
    public String floorRefId;
    public String apMacAddress;
    public String tagVendorData;
    public String locationMapHierarchy;
    public Number eventId;
    public String manufacturer;
    public MaxDetectedRssi maxDetectedRssi;
    public String ssid;
    public RawLocation rawLocation;
    public String locComputeType;
    public String subscriptionName;
    public Boolean associated;
    public String username;
    public Number timestamp;
    public String lastSeen;
    public Number confidenceFactor;
    public Number floorId;
    public LocationCoordinate locationCoordinate;
    public String deviceId;

    @Override
    public CMXTypes getType() {
        return CMXTypes.LOCATION_UPDATE;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Node createNode(Node parent, Map<String, Boolean> filter) {
        CMXDSLink.LOGGER.debug("In LocationUpdateNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "deviceId", deviceId, filter.get("deviceId"));
        NotificationUtils.createNode(n, "entity", entity, filter.get("entity"));
        NotificationUtils.createNode(n, "band", band, filter.get("band"));
        if(geoCoordinate != null) {
            geoCoordinate.createNode(n,"geoCoordinate");
        }
        NotificationUtils.createNode(n, "notificationType", notificationType, filter.get("notificationType"));
        NotificationUtils.createNode(n, "ipAddress", ipAddress, filter.get("ipAddress"));
        NotificationUtils.createNode(n, "bleTagInfo", bleTagInfo, filter.get("bleTagInfo"));
        NotificationUtils.createNode(n, "floorRefId", floorRefId, filter.get("floorRefId"));
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress, filter.get("apMacAddress"));
        NotificationUtils.createNode(n, "tagVendorData", tagVendorData, filter.get("tagVendorData"));
        NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy, filter.get("locationMapHierarchy"));
        NotificationUtils.createNode(n, "eventId", eventId, filter.get("eventId"));
        NotificationUtils.createNode(n, "manufacturer", manufacturer, filter.get("manufacturer"));
        if(maxDetectedRssi != null)
            maxDetectedRssi.createNode(n, "maxDetectedRssi");
        NotificationUtils.createNode(n, "ssid", ssid, filter.get("ssid"));
        if(rawLocation != null)
            rawLocation.createNode(n, "rawLocation");
        NotificationUtils.createNode(n, "locComputeType", locComputeType, filter.get("locComputeType"));
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName, filter.get("subscriptionName"));
        NotificationUtils.createNode(n, "associated", associated, filter.get("associated"));
        NotificationUtils.createNode(n, "username", username, filter.get("username"));
        NotificationUtils.createNode(n, "timestamp", timestamp, filter.get("timestamp"));
        NotificationUtils.createNode(n, "lastSeen", lastSeen, filter.get("lastSeen"));
        NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor, filter.get("confidenceFactor"));
        NotificationUtils.createNode(n, "floorId", floorId, filter.get("floorId"));
        if(locationCoordinate != null)
            locationCoordinate.createNode(n, "locationCoordinate");
        n.setMetaData(this);
        return n;
    }
}
