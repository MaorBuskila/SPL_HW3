package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

public class Follow extends Message {
    private String followOrUn;
    private String username;
    private final short OPCODE = 4;

    public Follow(String followOrUn, String username) {
        this.followOrUn = followOrUn;
        this.username = String.valueOf(username);
        System.out.println("debug");
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        boolean command;
        if (followOrUn.equals("0")) { // follow command
            command = database.follow(connectionId, username);
        }
        else { // unfollow command
            command = database.unfollow(connectionId, username);

        }
        if (command) {
            ACK ackMessage = new ACK(OPCODE, null);
            connections.send(connectionId, ackMessage);
        } else {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        }
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
    




