package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import java.text.ParseException;

public class PM extends Message {
    private  final short OPCODE = 6;
    private String username;
    private String content;
    private String dateAndTime;
    public PM(String username, String content, String dateAndTime) {
        this.username = username;
        this.content = content;
        this.dateAndTime=dateAndTime;
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
//        if (!database.getRegisterUsers().contains(connectionId) || database.getConnectionID_userName().contains(this.username)) {
//            User user = new User(username, password, birthday);
//            database.registerClient(connectionId, user);
//            ACK ackMessage = new ACK(OPCODE);
//            connections.send(connectionId , ackMessage);
//        }
//        else{
//            Error errorMessage = new Error(OPCODE);
//            connections.send(connectionId , errorMessage);
//        }
        if(!database.getRegisterUsers().containsKey(connectionId) || !database.getUser(connectionId).isLoggedIn() ||
        !database.getRegisterUsers().containsKey(database.getConnectionID_userName().get(this.username)) || !database.getUser(connectionId).getFollowing().containsKey(database.getConnectionID_userName().get(this.username)))
        {

            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId , errorMessage);

        }
        else
        {
            //TODO how iknow which words to filter
            String filteredMessage=this.content;
            for(String s: database.getForbiddenWords())
            {
                filteredMessage.replaceAll(s,"<filtered>");
            }
            database.addMessage(filteredMessage);
            ACK ackMessage = new ACK(OPCODE,null); //TODO Fix it
            connections.send(connectionId , ackMessage);
            //TODO notification
            // Notification notificationMessage=new Notification()
        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
