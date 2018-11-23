package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXConstants;
import cz.simac.cmxdslink.CMXDSLink;
import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.actions.Action;

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
    public Node createNode(Node parent, Map<String, Boolean> filter, Action filterAction) {
        CMXDSLink.LOGGER.debug("In LocationUpdateNotification::createNode() method");
        Node n = CMXNotificationManager.getOrCreate(parent, getDeviceId())
                .setDisplayName(getDeviceId())
                .setSerializable(false)
                .build();
        NotificationUtils.setAction(NotificationUtils.createNode(n, "deviceId", deviceId, filter.get("deviceId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "entity", entity, filter.get("entity")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "band", band, filter.get("band")),
                CMXConstants.FILTER_OUT, filterAction);
        if(geoCoordinate != null) {
            geoCoordinate.createNode(n,"geoCoordinate");
        }
        NotificationUtils.setAction(NotificationUtils.createNode(n, "notificationType", notificationType, filter.get("notificationType")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "ipAddress", ipAddress, filter.get("ipAddress")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "bleTagInfo", bleTagInfo, filter.get("bleTagInfo")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "floorRefId", floorRefId, filter.get("floorRefId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "apMacAddress", apMacAddress, filter.get("apMacAddress")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "tagVendorData", tagVendorData, filter.get("tagVendorData")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy, filter.get("locationMapHierarchy")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "eventId", eventId, filter.get("eventId")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "manufacturer", manufacturer, filter.get("manufacturer")),
                CMXConstants.FILTER_OUT, filterAction);
        if(maxDetectedRssi != null)
            maxDetectedRssi.createNode(n, "maxDetectedRssi");
        NotificationUtils.setAction(NotificationUtils.createNode(n, "ssid", ssid, filter.get("ssid")),
                CMXConstants.FILTER_OUT, filterAction);
        if(rawLocation != null)
            rawLocation.createNode(n, "rawLocation");
        NotificationUtils.setAction(NotificationUtils.createNode(n, "locComputeType", locComputeType, filter.get("locComputeType")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "subscriptionName", subscriptionName, filter.get("subscriptionName")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "associated", associated, filter.get("associated")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "username", username, filter.get("username")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "timestamp", timestamp, filter.get("timestamp")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "lastSeen", lastSeen, filter.get("lastSeen")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor, filter.get("confidenceFactor")),
                CMXConstants.FILTER_OUT, filterAction);
        NotificationUtils.setAction(NotificationUtils.createNode(n, "floorId", floorId, filter.get("floorId")),
                CMXConstants.FILTER_OUT, filterAction);
        if(locationCoordinate != null)
            locationCoordinate.createNode(n, "locationCoordinate");
        n.setMetaData(this);
        return n;
    }
}
