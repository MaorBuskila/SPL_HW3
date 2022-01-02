package bgu.spl.net.impl.BGSServer.Messages;

public class ACK{
    private final short OPCODE = 10;
    private short messageOPcode;
    private short logStateOPCODE = 7;
    private byte[][] optional;

    public ACK(short messageOPcode ,byte[][] optional) {

            this.messageOPcode = messageOPcode;
            this.optional=optional;


    }

    public String toString()  {
        return "ACK " + messageOPcode+optional.toString();
    }
}
