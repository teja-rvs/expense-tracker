package com.example.geektrust.service;

import com.example.geektrust.exception.UserLimitException;
import com.example.geektrust.model.DebtGraph;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository userRepository;
    private TotalDueService totalDueService = new TotalDueService();
    private  DebtGraph debtGraph;

    public UserService(){
        this.userRepository = UserRepository.getInstance();
        this.debtGraph = DebtGraph.getInstance();
    }

    public void addUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            if(checkUserLimit()){
                user = new User(userName);
                userRepository.create(user);
                totalDueService.addUser(user.getUserName());
                debtGraph.addNode(user.getUserName());
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

    public List<User> getAllUsers(){
        return new ArrayList(userRepository.getAllUsers().values());
    }
}
