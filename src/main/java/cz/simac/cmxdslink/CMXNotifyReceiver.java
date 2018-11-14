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
import java.util.stream.Collectors;

public class CMXNotifyReceiver {

    private Node rootNode;

    private InetSocketAddress address;

    private HttpServer server;

    private Object notification;

    private CMXHandler cmxHandler;

    public CMXNotifyReceiver(Node rootNode, InetSocketAddress address){
        this.rootNode = rootNode;
        this.address = address;
        this.notification = new Object();
        this.cmxHandler = new CMXHandler(notification);
    }

    public void run() throws IOException {
        System.out.println("in run() method");
        server = HttpServer.create(address, 0);
        server.createContext("/", this.cmxHandler);
        server.start();
        while (true) {
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
            catch(InterruptedException ie){ }
        }
    }

    private void addNotify(CMXNotify notify){
        System.out.println("In addNotify() method");
        notify.update(rootNode);
    }

    static class CMXHandler implements HttpHandler {

        public Object notification;

        private List<CMXNotify> notify = new ArrayList<>();

        public CMXHandler(Object notification){
            this.notification = notification;
        }

        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            String data;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
                data = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            synchronized(notification){
                notify.addAll(Arrays.asList(CMXNotify.encodeJSON(data)));
                notification.notify();
                try{
                    notification.wait(1000);
                }
                catch(InterruptedException ie){}
            }
            String response = "";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        public CMXNotify[] getNotify(){
            CMXNotify[] tmp = new CMXNotify[notify.size()];
            notify.toArray(tmp);
            notify.clear();
            return tmp;
        }
    }
}
