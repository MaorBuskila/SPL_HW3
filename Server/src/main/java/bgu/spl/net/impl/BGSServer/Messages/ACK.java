package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

public class ACK{
    private final short OPCODE = 10;
    private short messageOPcode;
    private

    public ACK(short messageOPcode) {
        this.messageOPcode = messageOPcode;
    }

    public String toString()  {
        return "ACK " + messageOPcode;
    }
}