package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;

public class LocationCoordinate {
    public Number z;
    public Number x;
    public Number y;
    public String unit;

    public Node createNode(Node parent, String name) {
        Node n = CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "z", z);
        NotificationUtils.createNode(n, "x", x);
        NotificationUtils.createNode(n, "y", y);
        NotificationUtils.createNode(n, "unit", unit);
        return n;
    }
}
