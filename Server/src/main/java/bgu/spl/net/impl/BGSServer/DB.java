package bgu.spl.net.impl.BGSServer;

import java.util.concurrent.ConcurrentHashMap;

public class DB {

    private ConcurrentHashMap<Integer,User> registerUsers = new ConcurrentHashMap();
    private ConcurrentHashMap<String,Integer> connectionID_userName = new ConcurrentHashMap();

    private static DB database = null;
    public static DB getInstance() {
        if (database == null) {
            database = new DB();
        }
        return database;
    }


    public void registerClient(int connectionId, User user) {
        registerUsers.putIfAbsent(connectionId ,user);
        connectionID_userName.putIfAbsent(user.getUsername(), connectionId);

    }

    public boolean follow(int userId , String followUserName) {
//        check if the user exist
        if (connectionID_userName.get(followUserName) != null ) {
            int followID = connectionID_userName.get(followUserName);
            //check that he is not already follow him
              if (registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().contains(followID)) {
                  registerUsers.get(followID).addFollowing(userId , registerUsers.get(userId)); //add following
                  registerUsers.get(userId).addFollower(followID , registerUsers.get(followID)); //add follower
                  return true;
            }
        }
        return false;
    }
    public boolean unfollow(int userId , String followUserName) {
//        check if the user exist
        if (connectionID_userName.get(followUserName) != null ) {
            int followID = connectionID_userName.get(followUserName);
            //check that he is not already follow him
            if (registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().contains(followID)) {
                registerUsers.get(followID).removeFollower(userId , registerUsers.get(userId)); //add following
                registerUsers.get(userId).removeFollower(followID , registerUsers.get(followID)); //add follower
                return true;
            }
        }
        return false;
    }

    public ConcurrentHashMap<Integer, User> getRegisterUsers() {
        return registerUsers;
    }

    public ConcurrentHashMap<String, Integer> getConnectionID_userName() {
        return connectionID_userName;
    }
}
