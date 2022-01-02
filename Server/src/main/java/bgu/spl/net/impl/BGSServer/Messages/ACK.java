package bgu.spl.net.impl.BGSServer.Messages;

public class ACK{
    private final short OPCODE = 10;
    private short messageOPcode;
    private short logStateOPCODE = 7;


    public ACK(short messageOPcode , String string) {
        if (messageOPcode == logStateOPCODE){
            this.messageOPcode = messageOPcode; //todo:change this
        }
        else{
            this.messageOPcode = messageOPcode;
        }

    }

    public String toString()  {
        return "ACK " + messageOPcode;
    }
}