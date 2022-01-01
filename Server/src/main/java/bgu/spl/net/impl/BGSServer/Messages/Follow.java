package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.srv.ConnectionHandler;

public class Follow extends Message{
//todo: check what happen if client have the same username?? does he follow all of them?
    private byte follow;
    private String username;
    public Follow(byte follow,String username)
    {
        this.follow=follow;
        this.username=username;
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {

        if(follow==0) { // this is a follow command
            //check if the username want to follow is log in
        boolean command = database.follow(connectionId, username);


            //follow if its ok
            //error or ACK messeage
        }
        else // this is unfollow command
        {
            boolean command = database.unfollow(connectionId, username);
            //check that he is follow him
            //check if the username want to follow is log in
            //unfollow if can
            //error or ACK MEsseage

        }
    }
    



}
