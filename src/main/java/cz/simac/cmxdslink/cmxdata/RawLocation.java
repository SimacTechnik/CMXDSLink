package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public class RawLocation {
    public Number rawX;
    public String unit;
    public Number rawY;

    public Node createNode(String name) {
        Node n = new Node(name, null, null, true);
        n.setDisplayName(name);
        NotificationUtils.createNode(n, "rawX", rawX);
        NotificationUtils.createNode(n, "unit", unit);
        NotificationUtils.createNode(n, "rawY", rawY);
        return n;
    }
}
