package cz.simac.cmxdslink;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CMXNotifyReceiver {

    private Node rootNode;

    private InetSocketAddress address;

    private HttpServer server;

    private final Object notification;

    private CMXHandler cmxHandler;

    private AtomicBoolean connected;

    public CMXNotifyReceiver(Node rootNode, InetSocketAddress address, AtomicBoolean connected){
        this.rootNode = rootNode;
        this.address = address;
        this.notification = new Object();
        this.connected = connected;
        this.cmxHandler = new CMXHandler(notification);
    }

    public void run() throws IOException {
        // System.out.println("in run() method");
        server = HttpServer.create(address, 0);
        server.createContext("/", this.cmxHandler);
        server.start();
        while(!connected.get()) {
            Thread.yield();
        }
        while (connected.get()) {
            try{
                CMXNotify[] tmpArray;
                synchronized (notification){
                    notification.wait();
                    tmpArray = cmxHandler.getNotify();
                    notification.notify();
                }
                for(CMXNotify tmp : tmpArray)
                    addNotify(tmp);
            }
            catch(InterruptedException ignore){ }
        }
    }

    private void addNotify(CMXNotify notify){
        // System.out.println("In addNotify() method");
        notify.update(rootNode);
    }

    static class CMXHandler implements HttpHandler {

        private final Object notification;

        private List<CMXNotify> notify = new ArrayList<>();

        private CMXHandler(Object notification){
            this.notification = notification;
        }

        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            String data;
            CMXNotify[] encoded = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
                data = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            synchronized(notification){
                if(data != null) {
                    encoded = CMXNotify.encodeJSON(data);
                    if(encoded != null)
                        notify.addAll(Arrays.asList(encoded));
                }
                notification.notify();
                try{
                    notification.wait(1000);
                }
                catch(InterruptedException ignore){}
            }
            String response = "";
            t.sendResponseHeaders(encoded == null ? 400 : 200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private CMXNotify[] getNotify(){
            CMXNotify[] tmp = notify.toArray(new CMXNotify[0]);
            notify.clear();
            return tmp;
        }
    }
}
