package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.Message;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private boolean shouldTerminate = false;
    private static DB database = DB.getInstance();
    private Connections<T> connections;
    private int connectionId;
    @Override

    /// what the fuck is this shit???
    public void start(int connectionId, Connections<T> connections) {
        this.connections = connections; // ??????
        this.connectionId = connectionId; //????????
    }

    @Override
    public void process(T message) {
    if (message instanceof Message) {
        ((Message) message).process(connectionId, connections , database); //TODO:implment!!! this is just check
     }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
