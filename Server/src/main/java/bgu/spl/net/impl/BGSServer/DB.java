package bgu.spl.net.impl.BGSServer;

import java.util.concurrent.ConcurrentHashMap;

public class DB {

    private ConcurrentHashMap<Integer,User> registerUsers = new ConcurrentHashMap();

        private static DB database = null;
    public static DB getInstance() {
        if (database == null) {
            database = new DB();
        }
        return database;
    }


    public void registerClient(int connectionId, User user) {
        registerUsers.putIfAbsent(connectionId ,user);
    }
}
