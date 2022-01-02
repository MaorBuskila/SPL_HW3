package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import java.util.Vector;

public class Post extends Message {
        private final short OPCODE = 5;
        private String content;
        private Vector<String> additionalUsers;

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
            user.post();
           for(User tmpuser : user.getFollowers().values()){
               tmpuser.addMessage(content);
           }
            extractUsers(content);
           for (String additionalUser : additionalUsers){
             User additionalTmpUser = database.getRegisterUsers().get(database.getConnectionID_userName().get(additionalUser));
               if(!user.getFollowers().contains(additionalTmpUser)){
                   additionalTmpUser.addMessage(content);
               }

           }

        }

    }

    public void extractUsers (String tmpContent){
        while (tmpContent.contains("@")){
            int tmp = tmpContent.indexOf("@");
            additionalUsers.add(tmpContent.substring(tmp+1, tmpContent.indexOf(" " , tmp)));
            tmpContent = tmpContent.substring(tmpContent.indexOf(" " , tmp));
        }

    }
}
