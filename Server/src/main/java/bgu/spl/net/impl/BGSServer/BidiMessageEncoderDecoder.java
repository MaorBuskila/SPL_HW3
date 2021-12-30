package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;

//Should be T or not??????????
public class BidiMessageEncoderDecoder implements MessageEncoderDecoder {

    private int len = 0;
    private byte[] bytes = new byte[1 << 10]; //TODO: check line size
    @Override
    public Object decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }

    private Mess popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
}

