package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;

public class MaxDetectedRssi {
    public Number antennaIndex;
    public Number slot;
    public Number rssi;
    public String apMacAddress;
    public String band;
    public Number lastHeardInSeconds;

    public Node createNode(Node parent, String name) {
        Node n = CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "antennaIndex", antennaIndex, true);
        NotificationUtils.createNode(n, "slot", slot, true);
        NotificationUtils.createNode(n, "rssi", rssi, true);
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress, true);
        NotificationUtils.createNode(n, "band", band, true);
        NotificationUtils.createNode(n, "lastHeardInSeconds", lastHeardInSeconds, true);
        return n;
    }
}
