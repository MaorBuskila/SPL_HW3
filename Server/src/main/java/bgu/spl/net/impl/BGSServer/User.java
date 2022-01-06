package bgu.spl.net.impl.BGSServer;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class User {


    private ConcurrentHashMap <Integer , User> followers;
    private ConcurrentHashMap <Integer, User> following;
    private String username;
    private String password;
    private Date birthday;
    private boolean isRegister=false;
    private boolean isLogIn=false;
    private short numberOfPost=0;
    //TODO TimeStamp
    private ConcurrentHashMap<User, Timestamp> followTime = new ConcurrentHashMap<>();
    private ConcurrentHashMap<User,Timestamp> timeLastMessageRecieved = new ConcurrentHashMap<>();
    private Vector<User> usersThatBlockMe;
    private BlockingQueue<String> messages;

    public User(String username, String password, Date birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.followers = new ConcurrentHashMap<>();
        this.following = new ConcurrentHashMap<>();
        this.messages = new LinkedBlockingQueue();
    }
    public boolean isRegistered() {
        return isRegister;
    }
    public boolean isLoggedIn() {
        return isLogIn;
    }
    public void login(){
        isLogIn =true;
    }
    public void logout(){
        isLogIn =false;
    }
    public void register()
    {
        isRegister=true;
    }
    public void unregister(){
        isRegister=false;
    }
    public String getPassword() {
        return password;
    }
    public ConcurrentHashMap<Integer, User> getFollowers() {
        return followers;
    }
    public ConcurrentHashMap<Integer, User> getFollowing() {
        return following;
    }
    public void addFollower(int id,User user) {
        followers.put(id,user);
    }
    public void removeFollower(int id, User user) {
        followers.remove(id,user);
    }
    public void addFollowing(int id,User user) {
        following.put(id,user);
        followTime.put(user,new Timestamp(System.currentTimeMillis()));
    }
    public void removeFollowing(int id,User user) {
        following.remove(id,user);
        followTime.remove(user,new Timestamp(System.currentTimeMillis()));
    }

    public void addMessage(String message) {
        try {
            messages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ///////////////Getters/////////////
    public short getAge()
    {

        LocalDate localDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (short)(Period.between(localDate, LocalDate.now()).getYears());
    }


    public String getUsername() {
        return username;
    }


    public void post()
    {
        numberOfPost++;
    }
    public short  getNumberOfPost()
    {
        return numberOfPost;
    }
    public short getNumberOfFollowers()
    {
        return (short)followers.size();
    }
    public short getNumberOfFollowing()
    {
        return (short)following.size();
    }
    public void addBlockMe(User user)
    {
        usersThatBlockMe.add(user);
    }
    public boolean isBlockedMe(User user) {
        return usersThatBlockMe.contains(user);
    }


}
