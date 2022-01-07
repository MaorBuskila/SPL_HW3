package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import java.util.Vector;

public class Post extends Message {
        private final short OPCODE = 5;
        private String content;
        private Vector<String> additionalUsers=new Vector<>();

    public Post(String content) {
        this.content = content;
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        }
        else {
            //ACK Notification
            ACK ackMessage=new ACK(OPCODE ,null);
            connections.send(connectionId,ackMessage);
            user.post();
           for(User followerUser : user.getFollowers().values()){
               database.addMessage(followerUser,content);
               if(followerUser.isLoggedIn())
               {
                   Notification notiMessage=new Notification((byte)'1', user.getUsername(),content );
                   connections.send(connectionId,notiMessage);
               }
           }
            extractUsers(content);
 //          if(!additionalUsers.containsAll(null)) {
               for (String additionalUser : additionalUsers) {
                   User additionalTmpUser = database.getRegisterUsers().get(database.getUserName_ConnectionID().get(additionalUser));
                   if (!user.getFollowers().contains(additionalTmpUser)) {
                       database.addMessage(additionalTmpUser,content);
                       if(additionalTmpUser.isLoggedIn())
                       {
                           Notification notiMessage=new Notification((byte)'1', user.getUsername(),content );
                           connections.send(connectionId,notiMessage);
                       }
                       //TODO notification
 //                  }

               }
           }

        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    public void extractUsers (String tmpContent){
        while (tmpContent.contains("@")){
            int tmp = tmpContent.indexOf("@");
            additionalUsers.add(tmpContent.substring(tmp+1, tmpContent.indexOf(" " , tmp)));
            tmpContent = tmpContent.substring(tmpContent.indexOf(" " , tmp));
        }

    }
}
