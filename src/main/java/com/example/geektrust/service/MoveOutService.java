package com.example.geektrust.service;

import com.example.geektrust.model.DebtGraph;
import com.example.geektrust.model.Split;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.TotalDueRepository;

import java.util.Map;
import java.util.HashMap;

public class MoveOutService {
    private DebtGraph debtGraph;
    private TotalDueService totalDueService;
    private TotalDueRepository totalDueRepository;

    public MoveOutService(){
        this.debtGraph = DebtGraph.getInstance();
        this.totalDueService = new TotalDueService();
        this.totalDueRepository = TotalDueRepository.getInstance();
    }

    public void move_out(User user){
        String userName = user.getUserName();
        Map<String, Map<String, Integer>> finalDues = totalDueRepository.getFinalDues();
        Map<String, Integer> dues = finalDues.get(userName);
        if(dues.isEmpty()){
            System.out.println("FAILURE");
        }
        else{
            int total = 0;
            for(Map.Entry<String, Integer> income : dues.entrySet()){
                total += income.getValue();
            }
            for(Map.Entry<String, Map<String, Integer>> outcome : finalDues.entrySet() ){
                if(outcome.getKey() != userName){
                    for(Map.Entry<String, Integer> due : outcome.getValue().entrySet()){
                        if(due.getKey() == userName){
                            total -= due.getValue();
                        }
                    }
                }
            }
            if(total == 0){
                debtGraph.removeNode(userName);
                totalDueService.removeUser(userName);
                System.out.println("SUCCESS");
            }else{
                System.out.println("FAILURE");
            }
        }
    }
}
