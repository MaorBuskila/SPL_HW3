package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

public class Block extends Message{
    private String username;
    public Block(String username) {
        this.username=username;

    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        User user =  database.getRegisterUsers().get(connectionId);
        if(user==null || !database.getUserName_ConnectionID().containsKey(this.username))
            return Error

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
