package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;

public class Login extends Message {

    private final int OPCODE = 2;
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
        {
            //Check if the user Exist if not Error

            //check if password are right if not Error
            //check if he is already logged in if yes Error
            if (captcha.equals("0")) {
                //drop error
            } else {
            }
        }
    }
}
