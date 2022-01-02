package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.newsfeed.NewsFeed;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
//        NewsFeed feed = new NewsFeed(); //one shared object
        String data = new String();
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () -> new RemoteCommandInvocationProtocol<>(data), //protocol factory
                ObjectEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
