package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

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
        User user =database.getRegisterUsers().get(connectionId);
        User tmpUser=database.getRegisterUsers().get(database.getUserName_ConnectionID().get(this.username));
        if(user==null ||
                !user.isLoggedIn() || //check if user is not logged in
                tmpUser==null|| //check if registers list not conteain the connection ID
                !user.getFollowing().contains(tmpUser) //check if the user is not follow the reciepient user
                || user.isBlocked(tmpUser))
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
            ACK ackMessage = new ACK(OPCODE,null);
            connections.send(connectionId , ackMessage);
            Notification notificationMessage = new Notification((byte)0,database.getUser(connectionId).getUsername(),this.content+" "+this.dateAndTime);
            int tmpUserNameID = database.getUserName_ConnectionID().get(this.username);
            User getTheMessageUser= database.getUser(tmpUserNameID);
            if(getTheMessageUser.isLoggedIn()) {
                connections.send(tmpUserNameID,notificationMessage);
            }
            else {
                getTheMessageUser.addUnReadMessage(notificationMessage);
            }


        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
