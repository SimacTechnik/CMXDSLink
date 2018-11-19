package cz.simac.cmxdslink;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import cz.simac.cmxdslink.cmxdata.CMXNotification;
import cz.simac.cmxdslink.cmxdata.CMXNotificationParser;
import cz.simac.cmxdslink.cmxdata.CMXTypes;
import org.dsa.iot.dslink.node.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Handles CMX Notifications (contains HTTP Server)
public class CMXNotifyReceiver {
    private InetSocketAddress address;
    private HttpServer server;

    public CMXNotifyReceiver(InetSocketAddress address) {
        this.address = address;
    }

    public void run() throws IOException {
        server = HttpServer.create(address, 0);
        server.start();
    }

    public HttpContext addContext(String path, Node parentNode, CMXTypes type) {
        return server.createContext(path, new CMXHandler(parentNode, type));
    }

    public void removeContext(String path) {
        server.removeContext(path);
    }

    static class CMXHandler implements HttpHandler {

        private final CMXNotificationManager manager;

        private final CMXTypes type;

        private CMXHandler(Node parentNode, CMXTypes type) {
            this.manager = new CMXNotificationManager(parentNode, type);
            this.type = type;
        }

        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            String data;
            // reading data stream from http request
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
                data = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            // encoding data, then notifying listenner to proccess new data and waiting for him to process them
            List<CMXNotification> notifications = Arrays.asList(CMXNotificationParser.Encode(data));
            Boolean nullOrType = notifications.stream().anyMatch(a -> a == null || a.getType() != type);
            if(!nullOrType) {
                notifications.stream().forEach(a -> manager.update(a));
            }
            t.sendResponseHeaders(nullOrType ? 400 : 200, 0);
            t.getResponseBody().close();
        }
    }
}
