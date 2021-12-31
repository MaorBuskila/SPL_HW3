package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.Register;

import java.nio.ByteBuffer;
import java.util.Arrays;

//Should be T or not??????????
public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private short opcode;
    private int len = 0;
    private byte[] bytes = new byte[1 << 10];
    private byte[] opBytes = new byte[2];
    @Override
    public Message decodeNextByte(byte nextByte) {

        if (nextByte == ';') {
            return popMessage();
        }
        if (len == 2) {
             opcode = bytesToShort(opBytes);
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    private void pushByte(byte nextByte) {
        if (len < 2){
            opBytes[len++] = nextByte;
        }
        else{
            bytes[len++] = nextByte;
        }
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

    }

    @Override
    public byte[] encode(Message message) {
        String result = message.toString();
        return result.getBytes();
    }

    private Message popMessage() {
        String result = new String(bytes);
        String[] arguments = result.split("\0");
        len = 0;

        switch (opcode){
                 case 1: //register
                    return new Register(arguments[0], arguments[1] ,arguments[2]);
//               case 2:
//                   return new Login();
//               case 3:
//                   return new Logout();
//               case 4:
//                   return new Follow();
//                case 5:
//                    return new Post();
//                case 6:
//                    return new PM():
//                case 7:
//                    return new LoggedIn();
//                case 8:
//                    return new State();
//                case 9:
//                    return new Notifiaction();
//                case 10:
        }
        return null;
    }
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}

