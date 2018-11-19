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
    String ADD_CMX_RECEIVER = "Remove CMX Receiver";
    String GROUP_BY = "Group by";
    ValueType NOTIFICATION_TYPE = ValueType.makeEnum(
            ASSOCIATION,
            LOCATION_UPDATE,
            MOVEMENT
    );
}
