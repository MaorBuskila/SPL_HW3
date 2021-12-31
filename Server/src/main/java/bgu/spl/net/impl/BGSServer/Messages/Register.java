package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register implements Message{

    private  final int OPCODE = 1;
    private String username;
    private String password;
    private Date birthday;
    private String pattern = "dd/MM/yyyy";
    private SimpleDateFormat format = new SimpleDateFormat(pattern);
    public Register(String username, String password, String birthday) {
        this.username = username;
        this.password = password;
        try {
            this.birthday = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(int connectionId, Connections connections) {
       // connections.addClientConnection(connectionId,);
    }
}
