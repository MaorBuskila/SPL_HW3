package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.Message;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DB {

    private ConcurrentHashMap<Integer, User> registerUsers = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Integer> connectionID_userName = new ConcurrentHashMap();
    private Vector<String> pmAndPostMessages=new Vector<>();
    private String[] forbiddenWords=new String[]{"Adir","Trump","Maor" } ;

    private static DB database = null;

    public static DB getInstance() {
        if (database == null) {
            database = new DB();
        }
        return database;
    }


    public void registerClient(int connectionId, User user) {
        registerUsers.put(connectionId, user);
        connectionID_userName.put(user.getUsername(), connectionId);

    }
    public void addMessage(String message)
    {
        pmAndPostMessages.add(message);
    }

    public boolean follow(int userId, String followUserName) {
//        check if the user exist
        if (connectionID_userName.contains(String.valueOf(followUserName))) { //TODO:value of is necssery with the real client?
            int toFollowID = connectionID_userName.get(followUserName);
            //check that he is logged in && not already follow him
            if (registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().contains(toFollowID)) {
                registerUsers.get(userId).addFollowing(userId, registerUsers.get(toFollowID)); //add following
                registerUsers.get(toFollowID).addFollower(userId, registerUsers.get(userId)); //add follower
                return true;
            }
        }
        return false;
    }

    public boolean unfollow(int userId, String followUserName) {
//        check if the user exist
        if (connectionID_userName.contains(followUserName)) {
            int followID = connectionID_userName.get(followUserName);
            //check that he is logged in && already follow him
            if (registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().contains(followID)) {
                registerUsers.get(userId).removeFollowing(followID, registerUsers.get(followID)); //remove following
                registerUsers.get(followID).removeFollower(userId, registerUsers.get(userId)); //remove follower
                return true;
            }
        }
        return false;
    }



    public ConcurrentHashMap<Integer, User> getRegisterUsers() {
        return registerUsers;
    }

    public String[] getForbiddenWords() {
        return forbiddenWords;
    }

    public ConcurrentHashMap<String, Integer> getConnectionID_userName() {
        return connectionID_userName;
    }

    public User getUser(int connectionId)
    {
        return registerUsers.get(connectionId);
    }
    public boolean isRegistered(int connectionId)
    {
        return registerUsers.containsKey(connectionId);
    }
}
