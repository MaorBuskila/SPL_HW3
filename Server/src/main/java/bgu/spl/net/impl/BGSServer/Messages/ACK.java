package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;

public class ACK implements Message {
    private final short opcode = 10;
    private short messageOPcode;

    public ACK(short messageOPcode) {

        this.messageOPcode = messageOPcode;
    }
    @Override
    public void process(int connectionId, Connections connections) {

    }
    public String toString()  {
        return "ACK " + messageOPcode;
    }
}