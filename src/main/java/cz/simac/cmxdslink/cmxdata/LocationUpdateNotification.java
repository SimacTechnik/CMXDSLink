package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.node.Node;

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
    public Node createNode() {
        CMXDSLink.LOGGER.debug("In LocationUpdateNotification::createNode() method");
        Node n = new Node(getDeviceId(), null, NotificationUtils.link, true);
        n.setDisplayName(getDeviceId());
        NotificationUtils.createNode(n, "deviceId", deviceId);
        NotificationUtils.createNode(n, "entity", entity);
        NotificationUtils.createNode(n, "band", band);
        if(geoCoordinate != null) {
            n.addChild(geoCoordinate.createNode("geoCoordinate"));
        }
        NotificationUtils.createNode(n, "notificationType", notificationType);
        NotificationUtils.createNode(n, "ipAddress", ipAddress);
        NotificationUtils.createNode(n, "bleTagInfo", bleTagInfo);
        NotificationUtils.createNode(n, "floorRefId", floorRefId);
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress);
        NotificationUtils.createNode(n, "tagVendorData", tagVendorData);
        NotificationUtils.createNode(n, "locationMapHierarchy", locationMapHierarchy);
        NotificationUtils.createNode(n, "eventId", eventId);
        NotificationUtils.createNode(n, "manufacturer", manufacturer);
        if(maxDetectedRssi != null)
            n.addChild(maxDetectedRssi.createNode("maxDetectedRssi"));
        NotificationUtils.createNode(n, "ssid", ssid);
        if(rawLocation != null)
            n.addChild(rawLocation.createNode("rawLocation"));
        NotificationUtils.createNode(n, "locComputeType", locComputeType);
        NotificationUtils.createNode(n, "subscriptionName", subscriptionName);
        NotificationUtils.createNode(n, "associated", associated);
        NotificationUtils.createNode(n, "username", username);
        NotificationUtils.createNode(n, "timestamp", timestamp);
        NotificationUtils.createNode(n, "lastSeen", lastSeen);
        NotificationUtils.createNode(n, "confidenceFactor", confidenceFactor);
        NotificationUtils.createNode(n, "floorId", floorId);
        if(locationCoordinate != null)
            n.addChild(locationCoordinate.createNode("locationCoordinate"));
        n.setMetaData(this);
        n.setSerializable(false);
        return n;
    }
}
