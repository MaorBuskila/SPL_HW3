package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl.shortToBytes;

public class Logstat extends Message {
    private final short OPCODE = 7;

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        String tmp="";
        User user = database.getRegisterUsers().get(connectionId);
        if (user == null || !user.isLoggedIn()) {
            Error errorMessage = new Error(OPCODE);
            connections.send(connectionId, errorMessage);
        } else {
//            for (User tmpUser : database.getRegisterUsers().values()) {
//                if (tmpUser.isLoggedIn() ) {
////                    byte[] age = (String.valueOf(tmpUser.getAge())).getBytes(StandardCharsets.UTF_8);
//                    byte[] age = shortToBytes(tmpUser.getAge());
//                    byte[] numberOfPost = shortToBytes(tmpUser.getNumberOfPost());
//                    byte[] numberOfFollowers = shortToBytes(tmpUser.getNumberOfFollowers());
//                    byte[] numberOfFollowing = shortToBytes(tmpUser.getNumberOfFollowing());
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//                    try {
//                        outputStream.write( age );
//                        outputStream.write( numberOfPost );
//                        outputStream.write( numberOfFollowers );
//                        outputStream.write( numberOfFollowing );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    byte bytes[] = outputStream.toByteArray( );
//                    for (byte aByte : bytes) {
////                        System.out.print(aByte);
//
//                    }
//                    String result = Arrays.toString(bytes);
////                    System.out.println(result);
//                    ACK ackMessage = new ACK(OPCODE, bytes);
//                    connections.send(connectionId, ackMessage);
//                }
//            }
//


            Vector<byte[]> vec = new Vector<>();
                    byte[][] bytes = new byte[2][4];
            for (User tmpUser : database.getRegisterUsers().values()) {
                if (tmpUser.isLoggedIn()) {
                    vec.add(shortToBytes(user.getAge()));
                    vec.add(String.valueOf(tmpUser.getAge()).getBytes(StandardCharsets.UTF_8));
                    vec.add(shortToBytes(user.getAge()));
                    vec.add(shortToBytes(user.getNumberOfPost()));
                    vec.add(shortToBytes(user.getNumberOfFollowing()));
//                    bytes[0] = shortToBytes(user.getAge());
//                    bytes[1] = shortToBytes(user.getNumberOfPost());
//                    bytes[2] = shortToBytes(user.getNumberOfFollowers());
//                    bytes[3] = shortToBytes(user.getNumberOfFollowing());
//                    byte[] out = new byte[bytes.length * bytes[0].length];
//                    for (int i = 0; i < bytes.length; i++) {
//                        for (int j = 0; j < bytes[i].length; j++) {
//                            out[i + (j * bytes.length)] = bytes[i][j];
//                        }
//                    }
                    ACK ackMessage = new ACK(OPCODE, vec);
                    connections.send(connectionId, ackMessage);
                }
            }
        }

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return null;
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}