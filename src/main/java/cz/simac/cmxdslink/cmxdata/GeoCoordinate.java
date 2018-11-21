package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.node.Node;

public class GeoCoordinate {
    public Number latitude;
    public Number longitude;
    public String unit;

    public Node createNode(String name){
        Node n = new Node(name, null, null, true);
        n.setDisplayName(name);
        NotificationUtils.createNode(n, "latitude", latitude);
        NotificationUtils.createNode(n, "longitude", longitude);
        NotificationUtils.createNode(n, "unit", unit);
        return n;
    }
}
