package bgu.spl.net.impl.BGSServer;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class User {


    private ConcurrentHashMap <Integer , User> followers;
    private ConcurrentHashMap <Integer, User> following;
    //private ConcurrentHashMap <String , User>
    private String username;
    private String password;
    private Date birthday;
    private boolean isRegister=false;
    private boolean isLogIn=false;
    private int numberOfPost=0;
    //TODO TimeStamp
    private ConcurrentHashMap<User, Timestamp> followTime;
    private ConcurrentHashMap<User,Timestamp> timeLastMessageRecieved;
    private Vector<User> usersThatBlockMe;

    public User(String username, String password, Date birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.followers = new ConcurrentHashMap<>();
        this.following = new ConcurrentHashMap<>();
    }
    public boolean isRegistered()
    {
        return isRegister;
    }
    public boolean isLoggedIn()
    {
        return isLogIn;
    }
    public String getPassword()
    {
        return password;
    }
    public ConcurrentHashMap<Integer, User> getFollowers() {
        return followers;
    }

    public ConcurrentHashMap<Integer, User> getFollowing() {
        return following;
    }
    public void addFollower(int id,User user)
    {
        followers.put(id,user);

    }
    public void addFollowing(int id,User user)
    {
        following.put(id,user);
        followTime.put(user,new Timestamp(System.currentTimeMillis()));
    }
    public int getAge()
    {
        //TODO why The ---------------------------------------
       return Period.between(LocalDate.of(birthday.getYear(),birthday.getMonth(),birthday.getDay()), LocalDate.now()).getYears();
    }
    public void post()
    {
        numberOfPost++;
    }
    public int  getNumberOfPost()
    {
        return numberOfPost;
    }
    public int getNumberOfFollowers()
    {
        return followers.size();
    }
    public int getNumberOfFollowing()
    {
        return following.size();
    }
    public void addBlockMe(User user)
    {
        usersThatBlockMe.add(user);
    }
    public boolean isBlockedMe(User user)
    {
        return usersThatBlockMe.contains(user);
    }



}
