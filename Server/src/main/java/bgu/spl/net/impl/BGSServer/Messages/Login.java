package bgu.spl.net.impl.BGSServer.Messages;

public class Login {

    private  final int OPCODE = 2;
    private String username;
    private String password;
    private byte captcha;

    public Login(String username,String password,byte captcha)
    {
        this.username=username;
        this.password=password;
        this.captcha=captcha;
    }

    public void process()
    {
        //Check if the user Exist if not Error
        //check if password are right if not Error
        //check if he is already logged in if yes Error
        if(captcha==0) {
            //drop error
        }
        // else for all ---- log in
    }
}
