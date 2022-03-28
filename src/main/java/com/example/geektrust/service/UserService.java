package com.example.geektrust.service;

import com.example.geektrust.exception.UserLimitException;
import com.example.geektrust.model.DebtGraph;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final TotalDueService totalDueService = new TotalDueService();
    private final DebtGraph debtGraph;

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

    private boolean checkUserLimit(){
        return userRepository.count() < 3;
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public Optional<User[]> validUsers(String[] userNames) {
        List<User> users = new ArrayList<>(userNames.length);
        for(String userName: userNames){
            User user = findByUserName(userName);
            if(user == null) return null;
            users.add(user);
        }
        return Optional.of((User[])users.toArray());
    }
}
