package com.example.geektrust.model;

import java.util.UUID;

public class User {
    private String id;
    private String userName;

    public User(String userName){
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
