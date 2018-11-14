package cz.simac.cmxdslink;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.dsa.iot.dslink.node.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// Handles CMX Notifications (contains HTTP Server)
public class CMXNotifyReceiver {

    private final Object notification;
    private Node rootNode;
    private InetSocketAddress address;
    private CMXHandler cmxHandler;
    private AtomicBoolean connected;

    public CMXNotifyReceiver(Node rootNode, InetSocketAddress address, AtomicBoolean connected) {
        this.rootNode = rootNode;
        this.address = address;
        this.notification = new Object();
        this.connected = connected;
        this.cmxHandler = new CMXHandler(notification);
    }

    public void run() throws IOException {
        // System.out.println("in run() method");
        HttpServer server = HttpServer.create(address, 0);
        server.createContext("/", this.cmxHandler);
        server.start();
        // waiting for connection
        while (!connected.get()) {
            Thread.yield(); // semi-passive waiting
        }
        // signal boolean if EFM is still connected to this link
        while (connected.get()) {
            try {
                CMXNotify[] tmpArray;
                // using passive waiting for thread synchronization
                synchronized (notification) {
                    notification.wait();
                    tmpArray = cmxHandler.getNotify();
                    notification.notify();
                }
                // set changes in nodes
                for (CMXNotify tmp : tmpArray)
                    tmp.update(rootNode);
            } catch (InterruptedException ignore) {
            }
        }
    }

    static class CMXHandler implements HttpHandler {

        // notification object for thread synchronization
        private final Object notification;

        private List<CMXNotify> notify = new ArrayList<>();

        private CMXHandler(Object notification) {
            this.notification = notification;
        }

        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            String data;
            CMXNotify[] encoded = null;
            // reading data stream from http request
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
                data = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            // encoding data, then notifying listenner to proccess new data and waiting for him to process them
            synchronized (notification) {
                if (data != null) {
                    encoded = CMXNotify.encodeJSON(data);
                    if (encoded != null)
                        notify.addAll(Arrays.asList(encoded));
                }
                notification.notify();
                try {
                    // waiting for listenner to take his data (if listenner is busy, waiting only for specific time)
                    notification.wait(10);
                } catch (InterruptedException ignore) {
                }
            }
            t.sendResponseHeaders(encoded == null ? 400 : 200, 0);
            t.getResponseBody().close();
        }

        private CMXNotify[] getNotify() {
            // return buffered data and clear the buffer
            CMXNotify[] tmp = notify.toArray(new CMXNotify[0]);
            notify.clear();
            return tmp;
        }
    }
}
