package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;

public interface Message {

    void process(int connectionId, Connections connections);
}
