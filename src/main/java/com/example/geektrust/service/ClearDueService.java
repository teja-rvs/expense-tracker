package com.example.geektrust.service;

import com.example.geektrust.model.DebtGraph;
import com.example.geektrust.model.User;
import com.example.geektrust.repository.TotalDueRepository;

import java.util.Map;
import java.util.HashMap;

public class ClearDueService {

    private TotalDueRepository totalDueRepository;
    private DebtGraph debtGraph;
    private TotalDueService totalDueService;

    public ClearDueService(){
        this.totalDueRepository = TotalDueRepository.getInstance();
        this.debtGraph = DebtGraph.getInstance();
        this.totalDueService = new TotalDueService();
    }

    public void settle(User payer, User payee, int amount){
        Map<String, Map<String, Integer>> finalDues = totalDueRepository.getFinalDues();
        String payerName = payer.getUserName();
        String payeeName = payee.getUserName();
        int due = finalDues.get(payerName).get(payeeName);
        if(due < amount * -1){
            System.out.println("INCORRECT_PAYMENT");
        }
        else{
            debtGraph.addDebt(payerName, payeeName, amount);
            debtGraph.simplifyGraph();
            totalDueService.updateFinalDues();
            totalDueService.getPayerPayeeDues(payerName, payeeName);
        }

    }
}
