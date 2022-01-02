package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

public class Logstat extends Message {
    @Override
    public void process(int connectionId, Connections connections, DB database) {

        for (User user : database.getRegisterUsers())

    }
}
