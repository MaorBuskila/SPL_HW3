package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;


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
            for (User tmpUser : database.getRegisterUsers().values()) {
                if (tmpUser.isRegistered()) {
                    bytes[0] = shortToBytes(user.getAge());
                    bytes[1] = shortToBytes(user.getNumberOfPost());
                    bytes[2] = shortToBytes(user.getNumberOfFollowers());
                    bytes[3] = shortToBytes(user.getNumberOfFollowing());
                    ACK ackMessage = new ACK(OPCODE, bytes);
                    connections.send(connectionId, ackMessage);
                }
            }
        }

    }
}
