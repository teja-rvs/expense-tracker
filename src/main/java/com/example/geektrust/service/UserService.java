package com.example.geektrust.service;

import com.example.geektrust.model.User;
import com.example.geektrust.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public UserService(){
        this.userRepository = UserRepository.getInstance();
    }

    public void addUser(String userName){
        User user = userRepository.findByUserName(userName);
        if(user == null){
            user = new User(userName);
            userRepository.create(user);
            System.out.println("SUCCESS");
        }
        else{
            System.out.println("User already added");
        }
    }
}
