package cz.simac.cmxdslink;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CMXDSLink extends DSLinkHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CMXDSLink.class);

    private Node superRoot;

    private final Random RANDOM = new Random();

    private DSLink link;

    private CMXNotifyReceiver receiver;

    private InetSocketAddress cmxListennerAddress = new InetSocketAddress(9090);

    public CMXDSLink(){
        super();
    }

    public CMXDSLink(InetSocketAddress address){
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
        Objects.getDaemonThreadPool().execute(() -> {
            receiver = new CMXNotifyReceiver(superRoot, cmxListennerAddress);
            try {
                receiver.run();
            }
            catch(IOException ie) {}
        });
    }
}
