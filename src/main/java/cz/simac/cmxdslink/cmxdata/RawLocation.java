package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;

public class RawLocation {
    public Number rawX;
    public String unit;
    public Number rawY;

    public Node createNode(Node parent, String name) {
        Node n = CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "rawX", rawX, true);
        NotificationUtils.createNode(n, "unit", unit, true);
        NotificationUtils.createNode(n, "rawY", rawY, true);
        return n;
    }
}
