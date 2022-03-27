package com.example.geektrust.service;

import com.example.geektrust.model.DebtGraph;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.TotalDueRepository;
import com.example.geektrust.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class TotalDueService {
    private TotalDueRepository totalDueRepository;
    private UserRepository userRepository;
    private DebtGraph debtGraph;

    public TotalDueService(){
        this.totalDueRepository = TotalDueRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.debtGraph = DebtGraph.getInstance();
    }

    public void addUser(String userName) {
        Map<String, Map<String, Integer>> finalDues = totalDueRepository.getFinalDues();
        List<User> users = getAllUsers();
        Map<String, Integer> dues = new HashMap();
        for(User user: users){
            String name = user.getUserName();
            if(userName != name){
                dues.put(name, 0);
                finalDues.get(name).put(userName, 0);
            }
        }
        totalDueRepository.addUser(userName, dues);
    }
    public List<User> getAllUsers(){
        return new ArrayList(userRepository.getAllUsers().values());
    }

    public void getUserDues(String userName){
        Map<String, Map<String, Integer>> finalDues = totalDueRepository.getFinalDues();
        Map<String, Integer> dues = finalDues.get(userName);
        for (Map.Entry<String, Integer> due : dues.entrySet()) {
            System.out.println(due.getKey() + " " + due.getValue());
        }
    }

    public void updateFinalDues(){
        Map<String, Map<String, Integer>> graph = debtGraph.getGraph();
        Map<String, Map<String, Integer>> finalDues =  totalDueRepository.getFinalDues();
        for (Map.Entry<String, Map<String, Integer>> node : graph.entrySet()) {
            for (Map.Entry<String, Integer> child : node.getValue().entrySet()){
                finalDues.get(node.getKey()).put(child.getKey(), child.getValue());;
            }
        }
    }
}
