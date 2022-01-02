package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.MessageEncoderDecoderImpl;

import java.nio.charset.StandardCharsets;

public class Notification extends Message{
    private final short OPCODE=9;
    private byte notificationType;
    private String postingUser;
    private String content;

    public Notification(byte notificationType, String postingUser, String content) {
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }


    @Override
    public void process(int connectionId, Connections connections, DB database) {

    }

    public String toString()
    {
        //byte b=0;
       // return shortToBytes(OPCODE)+notificationType+postingUser+b+content+b;
        return "9"+String.valueOf(notificationType)+String.valueOf(postingUser)+"0"+content+"0;";
    }
    public byte[] encode()
    {
        byte[] byteArray=new byte[5+postingUser.length()+content.length()];
        byte[] postUser=this.postingUser.getBytes(StandardCharsets.UTF_8);
        byte[] conTent=this.content.getBytes(StandardCharsets.UTF_8);
        byteArray[0]= MessageEncoderDecoderImpl.shortToBytes(OPCODE)[0];
        byteArray[1]=MessageEncoderDecoderImpl.shortToBytes(OPCODE)[1];
        byteArray[2]=notificationType;
        int k=3;
        for(int i=0;i<postingUser.length();i++)
        {
            byteArray[k++]=postUser[i];
        }
        // 0content0
        byteArray[k++]=0;
        for(int j=0;j<content.length();j++)
        {
            byteArray[k++]=conTent[j];
        }
        byteArray[k]=0;
        return byteArray;

    }
//    public static void main(String[] args)
//    {
//        Notification nt=new Notification((byte) 1,"Maor","adir");
//        byte[] tmpByte=nt.encode();
//        for(int i=0;i<tmpByte.length;i++){
//            System.out.print(tmpByte[i]);
//    }
//
//    }

}
