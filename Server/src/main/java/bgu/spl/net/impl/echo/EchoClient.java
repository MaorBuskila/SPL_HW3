package bgu.spl.net.impl.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(byte[][] args) throws IOException {

        if (args.length == 0) {
//            args = new String[]{"127.0.0.1", "040maor;"};
            byte[] bytes={1,'m','a','o','r',0,'a','b','c',0,10,'-',10,'-', 19,98,';'};
            byte[] bytes2={127,'.',0,'.',0,'.',1};
        //  args = new String[]{"127.0.0.1",bytes};
          args=new byte[][]{bytes2,bytes};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            System.out.println("sending message to server " + args[1]);
            out.write(args[1]);
            out.newLine();
            out.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
            System.out.println("message from server: " + line);
        }
    }
}
