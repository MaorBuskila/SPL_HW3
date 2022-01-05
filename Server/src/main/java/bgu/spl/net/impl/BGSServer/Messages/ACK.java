package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class ACK extends Message{
    private final short OPCODE = 10;
    private short messageOPcode;
    private byte[][] optional;
    private byte[] opByte = new byte[2];
    private byte[] msgByte = new byte[2];

    public ACK(short messageOPcode ,byte[][] optional) {

            this.messageOPcode = messageOPcode;
            this.optional=optional;


    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {

    }

    public byte[] encode()
    {
        opByte = shortToBytes(OPCODE);
        msgByte = shortToBytes(messageOPcode);
        byte delimeter = ';';
        if(optional!=null) {
            byte[] byteArray = new byte[4 + optional.length * optional[0].length];

            byteArray[0] = shortToBytes(OPCODE)[0];
            byteArray[1] = shortToBytes(OPCODE)[1];
            byteArray[2] = shortToBytes(messageOPcode)[0];
            byteArray[3] = shortToBytes(messageOPcode)[1];
            int k = 4;
            for (int i = 0; i < optional.length; i++) {
                for (int j = 0; j < optional[0].length; j++) {
                    byteArray[k++] = optional[i][j];

                }
            }
            return (""+OPCODE + messageOPcode + optional + ';').getBytes(StandardCharsets.UTF_8);
        }
        else
        {
//            byte[] allByteArray = new byte[5];
//            ByteBuffer buff = ByteBuffer.wrap(allByteArray);
//            buff.put(opByte);
//            buff.put(msgByte);
//            buff.put(delimeter);
//
//            byte[] combined = buff.array();
//
//
//            return combined;
            return (""+OPCODE+messageOPcode + ';').getBytes(StandardCharsets.UTF_8);
        }
    }

//    public String toString()  {
//        return "ACK " + messageOPcode+optional.toString();
//    }


}
