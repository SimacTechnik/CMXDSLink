package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXNotificationManager;
import org.dsa.iot.dslink.node.Node;

public class GeoCoordinate {
    public Number latitude;
    public Number longitude;
    public String unit;

    public Node createNode(Node parent, String name){
        Node n = CMXNotificationManager.getOrCreate(parent, name)
                .setDisplayName(name)
                .setSerializable(false)
                .build();
        NotificationUtils.createNode(n, "latitude", latitude, true);
        NotificationUtils.createNode(n, "longitude", longitude, true);
        NotificationUtils.createNode(n, "unit", unit, true);
        return n;
    }
}
