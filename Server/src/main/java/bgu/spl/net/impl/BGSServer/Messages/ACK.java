package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl;

import java.nio.charset.StandardCharsets;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class ACK extends Message{
    private final short OPCODE = 10;
    private short messageOPcode;
    private short logStateOPCODE = 7;
    private byte[][] optional;

    public ACK(short messageOPcode ,byte[][] optional) {

            this.messageOPcode = messageOPcode;
            this.optional=optional;


    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {

    }

    public byte[] encode()
    {
        byte[] byteArray=new byte[4+optional.length*optional[0].length];
     //   byte[] postUser=this.postingUser.getBytes(StandardCharsets.UTF_8);
       // byte[] conTent=this.content.getBytes(StandardCharsets.UTF_8);
        byteArray[0]= shortToBytes(OPCODE)[0];
        byteArray[1]= shortToBytes(OPCODE)[1];
        byteArray[2]=shortToBytes(messageOPcode)[0];
        byteArray[3]=shortToBytes(messageOPcode)[1];
        int k=4;
        for(int i=0;i<optional.length;i++)
        {
            for(int j=0;j<optional[0].length;j++)
            {
                byteArray[k++]=optional[i][j];

            }
        }
       return byteArray;
    }

    public String toString()  {
        return "ACK " + messageOPcode+optional.toString();
    }


}
