package cz.simac.cmxdslink;

import org.apache.commons.cli.*;
import org.dsa.iot.dslink.DSLinkFactory;

import java.net.InetSocketAddress;

public class Main {

    private static final int DEFAULT_PORT = 9090;

    private static final String DEFAULT_IP = "0.0.0.0";

    public static void main(String args[]) {
        CommandLineParser parser = new DefaultParser();
        Option ipOpt = new Option("i", "ip", true, "Network address where should be HTTPS Server listen to CMX Notifications");
        ipOpt.setRequired(false);
        Option portOpt = new Option("p", "port", true, "Port where should be HTTPS Server listen to CMX Notifications");
        portOpt.setRequired(false);
        Options options = new Options();
        options.addOption(ipOpt);
        options.addOption(portOpt);
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException ignore) {
        }
        String networkAddress = DEFAULT_IP;
        int port = DEFAULT_PORT;
        if (cl.hasOption("port")) {
            try {
                port = Integer.parseInt(cl.getOptionValue("port"));
            } catch (NumberFormatException ignore) {
            }
        }
        if (cl.hasOption("ip")) {
            networkAddress = cl.getOptionValue("ip");
        }
        InetSocketAddress socket;
        try {
            socket = new InetSocketAddress(networkAddress, port);
        } catch (IllegalArgumentException e) {
            socket = new InetSocketAddress(DEFAULT_IP, DEFAULT_PORT);
        }
        if (socket.isUnresolved()) {
            socket = new InetSocketAddress(DEFAULT_IP, DEFAULT_PORT);
        }
        DSLinkFactory.start(args, new CMXDSLink(socket));
    }
}
