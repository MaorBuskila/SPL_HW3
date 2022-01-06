package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class Logstat extends Message {
    private final short OPCODE = 7;

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        byte[] bytes = new byte[8];

        String tmp="";
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        } else {
            for (User tmpUser : database.getRegisterUsers().values()) {
                if (tmpUser.isLoggedIn()) {
                  //  bytes[0] = shortToBytes(user.getAge())[0];
                    //bytes[1] = shortToBytes(user.getAge())[1];
                    //bytes[1] = shortToBytes(user.getNumberOfPost());
                    //bytes[2] = shortToBytes(user.getNumberOfFollowers());
                    //bytes[3] = shortToBytes(user.getNumberOfFollowing());
                    short age=user.getAge();
                    short numberofPost=user.getNumberOfPost();
                    short numberOfFollowers=user.getNumberOfFollowers();
                    short numbereOfFollowing= user.getNumberOfFollowing();
                    byte[] byteArray=new byte[8];
//                    byte[] tmpByteArray=new byte[2];
//
//                    tmpByteArray=shortToBytes(age);
                    byteArray[0]=shortToBytes(age)[0];
                    byteArray[1]=shortToBytes(age)[1];
                    byteArray[2]=shortToBytes(numberofPost)[0];
                    byteArray[3]=shortToBytes(numberofPost)[1];
                    byteArray[4]=shortToBytes(numberOfFollowers)[0];
                    byteArray[5]=shortToBytes(numberOfFollowers)[1];
                    byteArray[6]=shortToBytes(numbereOfFollowing)[0];
                    byteArray[7]=shortToBytes(numbereOfFollowing)[1];
                    ACK ackMessage = new ACK(OPCODE, byteArray  );
//                    for(int i=0;i<byteArray.length;i++)
//                        System.out.print(byteArray[i]+ " ");
                    connections.send(connectionId, ackMessage);
                }
            }
        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}