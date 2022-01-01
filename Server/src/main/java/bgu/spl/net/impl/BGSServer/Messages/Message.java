package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

public abstract class Message {

    public abstract void process(int connectionId, Connections connectionsת , DB database);
}
