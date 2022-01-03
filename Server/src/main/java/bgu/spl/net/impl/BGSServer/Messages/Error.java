package bgu.spl.net.impl.BGSServer.Messages;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class Error {

    private final short OPCODE = 11;
    private short messageOPcode;

    public Error(short messageOPcode) {
        this.messageOPcode = messageOPcode;
    }

    public String toString()  {
        return "EROOR " + messageOPcode;
    }


    public byte[] encode()
    {
        byte[] byteArray=new byte[4];

        byteArray[0]= shortToBytes(OPCODE)[0];
        byteArray[1]= shortToBytes(OPCODE)[1];
        byteArray[2]=shortToBytes(messageOPcode)[0];
        byteArray[3]=shortToBytes(messageOPcode)[1];

        return byteArray;
    }
}

