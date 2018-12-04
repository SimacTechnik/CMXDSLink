package cz.simac.cmxdslink;

import org.dsa.iot.dslink.DSLinkFactory;

public class Main {

    public static void main(String args[]) {
        DSLinkFactory.start(new String[] {"-b", "http://127.0.0.1:37293/conn", "-l", "debug", "-d", "../dslink.json"}, new CMXDSLink());
    }
}
