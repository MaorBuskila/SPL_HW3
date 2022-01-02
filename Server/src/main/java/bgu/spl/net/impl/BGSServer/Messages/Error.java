package bgu.spl.net.impl.BGSServer.Messages;

public class Error {

    private final short OPCODE = 11;
    private short messageOPcode;

    public Error(short messageOPcode) {
        this.messageOPcode = messageOPcode;
    }

    public String toString()  {
        return "EROOR " + messageOPcode;
    }
}

