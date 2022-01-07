package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;


public class Stats extends Message{
    private final short OPCODE = 8;
    private String[] listOfUserNames;

    public Stats(String usernames)
    {
        listOfUserNames=usernames.split("\\|");
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        } else {
            for (String tmpUserName : listOfUserNames) {
                int tmpUserNameID = database.getUserName_ConnectionID().get(tmpUserName);
                User listUser = database.getRegisterUsers().get(tmpUserNameID);
                if (listUser != null) {
                    short age = listUser.getAge();
                    short numberofPost = listUser.getNumberOfPost();
                    short numberOfFollowers = listUser.getNumberOfFollowers();
                    short numbereOfFollowing = listUser.getNumberOfFollowing();
                    byte[] byteArray = new byte[8];
//                    byte[] tmpByteArray=new byte[2];
//
//                    tmpByteArray=shortToBytes(age);
                    byteArray[0] = shortToBytes(age)[0];
                    byteArray[1] = shortToBytes(age)[1];
                    byteArray[2] = shortToBytes(numberofPost)[0];
                    byteArray[3] = shortToBytes(numberofPost)[1];
                    byteArray[4] = shortToBytes(numberOfFollowers)[0];
                    byteArray[5] = shortToBytes(numberOfFollowers)[1];
                    byteArray[6] = shortToBytes(numbereOfFollowing)[0];
                    byteArray[7] = shortToBytes(numbereOfFollowing)[1];
                    ACK ackMessage = new ACK(OPCODE, byteArray);
//                    for(int i=0;i<byteArray.length;i++)
//                        System.out.print(byteArray[i]+ " ");
                    connections.send(connectionId, ackMessage);
                } else {
                    Error errorMessage = new Error(OPCODE);
                    connections.send(connectionId, errorMessage);
                    return;
                }

            }
        }
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
