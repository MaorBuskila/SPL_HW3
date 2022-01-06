package bgu.spl.net.impl.BGSServer;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DB {

    private ConcurrentHashMap<Integer, User> registerUsers = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Integer> userName_ConnectionID = new ConcurrentHashMap();
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
        userName_ConnectionID.put(user.getUsername(), connectionId);
        user.register();

    }
    public void addMessage(User user, String message)
    {
        pmAndPostMessages.add(message);
        user.addMessage(message);
    }

    public boolean follow(int userId, String followUserName) {
//        check if the user exist
        if (userName_ConnectionID.containsKey(followUserName)) {
            int toFollowID = userName_ConnectionID.get(followUserName);
            //check that he is logged in && not already follow him
            if (registerUsers.containsKey(userId) && registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().containsKey(toFollowID)) {
                registerUsers.get(userId).addFollowing(userId, registerUsers.get(toFollowID)); //add following
                registerUsers.get(toFollowID).addFollower(userId, registerUsers.get(userId)); //add follower
                return true;
            }
        }
        return false;
    }

    public boolean unfollow(int userId, String followUserName) {
//        check if the user exist
        if (userName_ConnectionID.containsKey(followUserName)) {
            int followID = userName_ConnectionID.get(followUserName);
            //check that he is logged in && already follow him
            if (registerUsers.containsKey(userId)  && registerUsers.get(userId).isLoggedIn() && !registerUsers.get(userId).getFollowing().contains(followID)) {
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

    public ConcurrentHashMap<String, Integer> getUserName_ConnectionID() {
        return userName_ConnectionID;
    }

    public User getUser(int connectionId)
    {
        return registerUsers.get(connectionId);
    }
    public boolean isRegistered(int connectionId)
    {
        return registerUsers.containsKey(connectionId);
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

}
