package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;


public class Stats extends Message{
    private final short OPCODE = 8;
    private String[] listOfUserNames;

    public Stats(String usernames)
    {
        listOfUserNames=usernames.split("|");
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        byte[][] bytes = new byte[4][2];
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        } else {
            for (String tmpUserName : listOfUserNames) {
                int tmpUserNameID = database.getUserName_ConnectionID().get(tmpUserName);
                User listUser = database.getRegisterUsers().get(tmpUserNameID);
                if (listUser!=null) {
                    bytes[0] = shortToBytes(listUser.getAge());
                    bytes[1] = shortToBytes(listUser.getNumberOfPost());
                    bytes[2] = shortToBytes(listUser.getNumberOfFollowers());
                    bytes[3] = shortToBytes(listUser.getNumberOfFollowing());
                    ACK ackMessage = new ACK(OPCODE, bytes);
                    connections.send(connectionId, ackMessage);
                }
                else {
                    Error errorMessage = new Error(OPCODE);
                    connections.send(connectionId, errorMessage);
                    return;
                }
            }
        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
