package bgu.spl.net.impl.BGSServer;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private ConcurrentHashMap <Integer , User> followers;
    private ConcurrentHashMap <Integer, User> following;
    //private ConcurrentHashMap <String , User>
    private String username;
    private String password;
    private Date birthday;

    public User(String username, String password, Date birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.followers = new ConcurrentHashMap<>();
        this.following = new ConcurrentHashMap<>();
    }
}
