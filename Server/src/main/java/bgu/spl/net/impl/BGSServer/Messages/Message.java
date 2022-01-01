package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;

public abstract class Message {

    public abstract void process(int connectionId, Connections connections);
}
