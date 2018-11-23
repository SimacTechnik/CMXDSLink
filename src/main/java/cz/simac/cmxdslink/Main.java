package cz.simac.cmxdslink;

import org.dsa.iot.dslink.DSLinkFactory;

public class Main {

    public static void main(String args[]) {
        DSLinkFactory.start(new String[]{"--broker", "http://127.0.0.1:40265/conn", "-d", "../dslink.json", "--log", "debug"}, new CMXDSLink());
    }
}
