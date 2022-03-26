package com.example.geektrust.service;

import com.example.geektrust.exception.UserLimitException;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public UserService(){
        this.userRepository = UserRepository.getInstance();
    }

    public void addUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            if(checkUserLimit()){
                user = new User(userName);
                userRepository.create(user);
                System.out.println("SUCCESS");
            }
            else{
                try{
                    throw new UserLimitException("HOUSEFUL");
                }catch(UserLimitException err){
                    System.out.println(err.getMessage());
                }
            }
        }
        else{
            System.out.println("User already added");
        }
    }

    public boolean checkUserLimit(){
        return userRepository.count() < 3;
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
