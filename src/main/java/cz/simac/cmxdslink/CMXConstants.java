package cz.simac.cmxdslink;

import org.dsa.iot.dslink.node.value.ValueType;

public interface CMXConstants {
    String ASSOCIATION = "Association";
    String LOCATION_UPDATE = "Location Update";
    String MOVEMENT = "Movement";
    String NAME = "Name";
    String TYPE = "Type";
    String URL = "URL";
    String RM_CMX_RECEIVER = "Remove CMX Receiver";
    String ADD_CMX_RECEIVER = "Add CMX Receiver";
    String GROUP_BY = "Group by";
    String DEVICE_ID = "deviceId";
    String RESET_FILTER = "Reset filter";
    String FILTER_OUT = "Filter out";
    ValueType NOTIFICATION_TYPE = ValueType.makeEnum(
            ASSOCIATION,
            LOCATION_UPDATE,
            MOVEMENT
    );
}
