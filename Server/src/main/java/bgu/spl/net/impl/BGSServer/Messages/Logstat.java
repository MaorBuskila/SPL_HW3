package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

public class Logstat extends Message {
    private final short OPCODE = 7;
    @Override
    public void process(int connectionId, Connections connections, DB database) {
        String[] arguments = new String[4];
        User user  = database.getRegisterUsers().get(connectionId);
        if(user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        }
        else {
            for (User tmpUser : database.getRegisterUsers().values()) {
                if (tmpUser.isLoggedIn()){
                    arguments[0] = user.getAge();
                }
            }
        }

    }
}