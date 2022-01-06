package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

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
        if(!database.getRegisterUsers().containsKey(connectionId) ||
                !database.getUser(connectionId).isLoggedIn() || //check if user is not logged in
                !database.getRegisterUsers().containsKey(database.getUserName_ConnectionID().get(this.username)) || //check if registers list not conteain the connection ID
                !database.getUser(connectionId).getFollowing().containsKey(database.getUserName_ConnectionID().get(this.username))) //check if the user is not follow the reciepient user
        {

            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId , errorMessage);

        }
        else {
            String filteredMessage=this.content;
            for(String s: database.getForbiddenWords())
            {
                filteredMessage.replaceAll(s,"<filtered>");
            }
            database.addMessage(database.getUser(database.getUserName_ConnectionID().get(username)), filteredMessage);

//TODO: didnt do something with Time' impliement this with queue when he is not log in.


            ACK ackMessage = new ACK(OPCODE,null);
            connections.send(connectionId , ackMessage);
            Notification notificationMessage = new Notification((byte)0,database.getUser(connectionId).getUsername(),this.content);
            int tmpUserNameID = database.getUserName_ConnectionID().get(this.username);
            connections.send(tmpUserNameID,notificationMessage);
            //TODO notification

        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
