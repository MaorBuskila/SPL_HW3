package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.DB;



public class Logout extends Message {
    private  final short OPCODE = 3;
    private ACK ack = new ACK(OPCODE);
    @Override
    public void process(int connectionId, Connections connections, DB database) {
        if (database.getRegisterUsers().get(connectionId).isLoggedIn()){
            database.getRegisterUsers().get(connectionId).logout();
            connections.disconnect(connectionId);
            //send ACK TODO:Implement in client!! after he get the ack he shoulde be terminated
            connections.send(connectionId ,ack);
        }
        else{
            //connections.send(connectionId ,ERROR); TODO:Implement this!!!!
        }
    }
}
