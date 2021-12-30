package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    @Override
    public void start(int connectionId, Connections<T> connections) {
//        connections.addClientConnection(connectionId ,ch);
    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
