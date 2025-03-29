package com.example.pracainzynierska;

public class FriendObject {
    String nick, auth;

    public FriendObject(String nick, String friendID) {
        this.nick = nick;
        this.auth = friendID;
    }

    public String getNick() {
        return nick;
    }

    public String getAuth() {
        return auth;
    }
}
