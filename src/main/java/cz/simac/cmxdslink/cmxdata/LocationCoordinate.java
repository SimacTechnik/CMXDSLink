package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public class LocationCoordinate {
    public Number z;
    public Number x;
    public Number y;
    public String unit;

    public Node createNode(String name) {
        Node n = new Node(name, null, NotificationUtils.link, true);
        n.setDisplayName(name);
        NotificationUtils.createNode(n, "z", z);
        NotificationUtils.createNode(n, "x", x);
        NotificationUtils.createNode(n, "y", y);
        NotificationUtils.createNode(n, "unit", unit);
        return n;
    }
}
