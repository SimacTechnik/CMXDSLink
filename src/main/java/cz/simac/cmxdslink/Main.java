package cz.simac.cmxdslink;

import org.apache.commons.cli.*;
import org.dsa.iot.dslink.DSLinkFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final int DEFAULT_PORT = 9090;

    private static final String DEFAULT_IP = "0.0.0.0";

    public static void main(String args[]) {
        CommandLineParser parser = new DefaultParser();
        Option ipOpt = new Option("i", "ip", true, "Network address where should be HTTPS Server listen to CMX Notifications");
        ipOpt.setRequired(false);
        Option portOpt = new Option("p", "port", true, "Port where should be HTTPS Server listen to CMX Notifications");
        portOpt.setRequired(false);
        Option helpOpt = new Option("h", "help", false, "Prints this help");
        helpOpt.setRequired(false);
        Options options = new Options();
        options.addOption(ipOpt);
        options.addOption(portOpt);
        options.addOption(helpOpt);
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException ignore) {
        }
        String networkAddress = DEFAULT_IP;
        int port = DEFAULT_PORT;
        if (cl != null && cl.hasOption("port")) {
            try {
                port = Integer.parseInt(cl.getOptionValue("port"));
            } catch (NumberFormatException ignore) {
            }
        }
        if (cl != null && cl.hasOption("ip")) {
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
        List<String> argsList = Arrays.asList(args);
        argsList.remove("ip");
        argsList.remove("i");
        argsList.remove("port");
        argsList.remove("p");
        DSLinkFactory.start(argsList.toArray(new String[0]), new CMXDSLink(socket));
        if (options.hasOption("help")){
            printHelp(ipOpt);
            printHelp(portOpt);
        }
    }

    private static void printHelp(Option opt){
        System.out.println("    --"+opt.getLongOpt()+", -"+opt.getOpt());
        System.out.println("      "+opt.getDescription());
    }
}
