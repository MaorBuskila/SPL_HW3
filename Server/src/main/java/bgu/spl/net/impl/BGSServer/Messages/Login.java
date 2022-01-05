package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;
import bgu.spl.net.impl.BGSServer.User;

public class Login extends Message {

    private final Short OPCODE = 2;
    private String username;
    private String password;
    private String captcha;

    public Login(String username, String password, String captcha) {
        this.username = username;
        this.password = password;
        this.captcha = captcha;
    }

    @Override
    public void process(int connectionId, Connections connections, DB database) {
        User user =  database.getRegisterUsers().get(connectionId);
            //Check if the user Exist if not Error
            //check if password are right if not Error
            //check if he is already logged in
            if (user == null || !user.getPassword().equals(password) || captcha.equals("0") || user.isLoggedIn()) {
                System.out.println(user +" = null");
                System.out.println(user.getPassword() +" = " + password );
                System.out.println(captcha + " = 0");
                System.out.println(user.isLoggedIn() + " = true ");
                Error errorMessage = new Error(OPCODE);
                connections.send(connectionId , errorMessage);
            }
             else {
                user.login();
                ACK ackMessage = new ACK(OPCODE, null);
                connections.send(connectionId , ackMessage);
            }
        //TODO notification
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
