package com.example.geektrust.repository;

import com.example.geektrust.model.User;

import java.util.Map;
import java.util.HashMap;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository(){}

    public static UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    private Map<String, User> userMap = new HashMap();

    public void create(User user){
        userMap.put(user.getUserName(), user);
    }

    public User findByUserName(String userName){
        return userMap.get(userName);
    }
}
