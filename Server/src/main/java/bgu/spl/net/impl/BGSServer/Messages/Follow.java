package bgu.spl.net.impl.BGSServer.Messages;

public class Follow {

    private byte follow;
    private String username;
    public Follow(byte follow,String username)
    {
        this.follow=follow;
        this.username=username;
    }

    public void process()
    {
        if(follow==0) { // this is a follow command
            //check that he is not already follow him
            //check if the username want to follow is log in
            //follow if its ok
            //error or ACK messeage
        }
        else // this is unfollow command
        {
            //check that he is follow him
            //check if the username want to follow is log in
            //unfollow if can
            //error or ACK MEsseage

        }
    }


}
