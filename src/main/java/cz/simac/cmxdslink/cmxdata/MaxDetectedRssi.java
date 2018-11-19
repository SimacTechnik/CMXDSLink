package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public class MaxDetectedRssi {
    public Number antennaIndex;
    public Number slot;
    public Number rssi;
    public String apMacAddress;
    public String band;
    public Number lastHeardInSeconds;

    public Node createNode(String name) {
        Node n = new Node(name, null, null, true);
        n.setDisplayName(name);
        NotificationUtils.createNode(n, "antennaIndex", antennaIndex);
        NotificationUtils.createNode(n, "slot", slot);
        NotificationUtils.createNode(n, "rssi", rssi);
        NotificationUtils.createNode(n, "apMacAddress", apMacAddress);
        NotificationUtils.createNode(n, "band", band);
        NotificationUtils.createNode(n, "lastHeardInSeconds", lastHeardInSeconds);
        return n;
    }
}
