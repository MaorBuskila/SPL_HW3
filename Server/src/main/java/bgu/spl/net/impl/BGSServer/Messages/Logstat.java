package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class Logstat extends Message {
    private final short OPCODE = 7;

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        byte[][] bytes = new byte[4][2];
        String tmp="";
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        } else {
            for (User tmpUser : database.getRegisterUsers().values()) {
                if (tmpUser.isLoggedIn()) {
                    bytes[0] = shortToBytes(user.getAge());
                    bytes[1] = shortToBytes(user.getNumberOfPost());
                    bytes[2] = shortToBytes(user.getNumberOfFollowers());
                    bytes[3] = shortToBytes(user.getNumberOfFollowing());
                    ACK ackMessage = new ACK(OPCODE, bytes); // TODO Fix it
                    connections.send(connectionId, ackMessage);
                }
            }
        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}