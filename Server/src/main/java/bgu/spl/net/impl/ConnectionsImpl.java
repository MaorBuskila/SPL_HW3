package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T>  implements Connections<T> {

    private ConcurrentHashMap<Integer , ConnectionHandler<T>> activeClient = new ConcurrentHashMap<>() ;
    @Override
    public boolean send(int connectionId, T msg) {
        if (activeClient.get(connectionId) == null)
            return false;

        activeClient.get(connectionId).send(msg);
//        System.out.println("Sending " + msg + "client number: " + connectionId);
        return true;
    }


    @Override
    public void broadcast(T msg) {
        System.out.println("Sending BroadCast to all active client");
        for (int connectionId : activeClient.keySet())
            this.send(connectionId , msg);

    }

    @Override
    public void disconnect(int connectionId) {
        if (activeClient.contains(connectionId)) {
            System.out.println("Client - " + connectionId + " disconnected");
        }

    }

    private void addClientConnection (int connectionId , ConnectionHandler<T> ch){
        if (activeClient.containsKey(ch)) {
            System.out.println("Client Already Connected");
        }
        else{
            activeClient.put(connectionId , ch);
        }
    }
}
