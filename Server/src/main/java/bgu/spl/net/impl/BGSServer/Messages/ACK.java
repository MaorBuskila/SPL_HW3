package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

public class ACK extends Message {
    private final short opcode = 10;
    private short messageOPcode;

    public ACK(short messageOPcode) {

        this.messageOPcode = messageOPcode;
    }
    @Override
    public void process(int connectionId, Connections connections, DB database) {

    }
    public String toString()  {
        return "ACK " + messageOPcode;
    }
}