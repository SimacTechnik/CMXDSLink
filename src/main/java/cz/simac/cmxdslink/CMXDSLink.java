package cz.simac.cmxdslink;

import com.sun.net.httpserver.HttpContext;
import cz.simac.cmxdslink.cmxdata.CMXTypes;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.Objects;
import org.dsa.iot.dslink.util.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CMXDSLink extends DSLinkHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CMXDSLink.class);

    private Node superRoot;

    private DSLink link;

    private CMXNotifyReceiver receiver;

    private InetSocketAddress cmxListennerAddress = new InetSocketAddress(9090);

    private Map<String, Node> contexts = new HashMap<>();

    public CMXDSLink() {
        super();
    }

    public CMXDSLink(InetSocketAddress address) {
        super();
        cmxListennerAddress = address;
    }

    @Override
    public boolean isResponder() {
        return true;
    }

    @Override
    public void onResponderConnected(final DSLink link) {
        LOGGER.info("Connected");
        this.link = link;
    }

    @Override
    public void onResponderInitialized(final DSLink link) {
        LOGGER.info("Initialized");
        superRoot = link.getNodeManager().getSuperRoot();
        makeAddCMX();
        receiver = new CMXNotifyReceiver(cmxListennerAddress);
        try {
            receiver.run();
        } catch (IOException ignore) { }
    }

    private void makeAddCMX() {
        Action act = new Action(Permission.READ, event -> handleAddCMX(event));
        act.addParameter(new Parameter(CMXConstants.NAME, ValueType.STRING, new Value("")));
        act.addParameter(new Parameter(CMXConstants.TYPE, CMXConstants.NOTIFICATION_TYPE, new Value(CMXConstants.NOTIFICATION_TYPE.getEnums().toArray(new String[0])[0])));
        act.addParameter(new Parameter(CMXConstants.URL, ValueType.STRING, new Value("/")));
        Node anode = superRoot.getChild(CMXConstants.ADD_CMX_RECEIVER, true);
        if(anode == null) superRoot.createChild(CMXConstants.ADD_CMX_RECEIVER, true).setAction(act).build().setSerializable(false);
        else anode.setAction(act);
    }

    private void handleAddCMX(ActionResult event) {
        // get parameter values
        String name = event.getParameter(CMXConstants.NAME).getString();
        String type = event.getParameter(CMXConstants.TYPE).getString();
        String path = event.getParameter(CMXConstants.URL).getString();
        // duplicity handling of context path
        if(!contexts.containsKey(path)) {
            // duplicity handling of context name
            for(Node nodeContext : contexts.values()) {
                if(nodeContext.getDisplayName().equals(name)) return;
            }

            //convert parameter enum to CMXTypes enum
            CMXTypes cmxType;
            switch(type) {
                case CMXConstants.ASSOCIATION:
                    cmxType = CMXTypes.ASSOCIATION;
                    break;
                case CMXConstants.LOCATION_UPDATE:
                    cmxType = CMXTypes.LOCATION_UPDATE;
                    break;
                case CMXConstants.MOVEMENT:
                    cmxType = CMXTypes.MOVEMENT;
                    break;
                default:
                    return;
            }

            // create node for created CMX receiver
            Node parentNode = superRoot.createChild(name, true)
                    .setDisplayName(name)
                    .setSerializable(false)
                    .build();

            // action for deleting created CMX receiver
            Action act = new Action(Permission.READ, ignore -> {
                // stop listening on this context
                receiver.removeContext(path);
                // delete node
                parentNode.delete(true);
            });

            // create option
            Node anode = parentNode.getChild(CMXConstants.RM_CMX_RECEIVER, true);
            if (anode == null) parentNode.createChild(CMXConstants.RM_CMX_RECEIVER, true).setAction(act).build().setSerializable(false);
            else anode.setAction(act);
            receiver.addContext(path, parentNode, cmxType);
        }
    }

    @Override
    public void onResponderDisconnected(DSLink link) {
        LOGGER.info("Disconnected");
    }
}
