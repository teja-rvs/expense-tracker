package com.example.geektrust.service;

import com.example.geektrust.model.*;
import com.example.geektrust.repository.ExpenseRepository;

import java.lang.Math;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private UserService userService;
    private DebtGraph debtGraph;

    public ExpenseService(){
        this.expenseRepository = ExpenseRepository.getInstance();
        this.userService = new UserService();
        this.debtGraph = DebtGraph.getInstance();
    }

    public boolean validateExpenseUsers(String[] users){
        for(String userName: users){
            User user = userService.findByUserName(userName);
            if(user == null) return false;
        }
        return true;
    }

    public Expense addExpense(int amount) {
        Expense expense = new Expense(amount);
        expenseRepository.create(expense);
        return expense;
    }

    public void addContributors(Expense expense, String userName){
        int amount = expense.getAmount();
        User user = userService.findByUserName(userName);
        Map<String, Contribution> contributions = new HashMap();
        Contribution contribution = new Contribution(user, amount);
        contributions.put(user.getUserName(), contribution);
        expense.setContributions(contributions);
    }

    public void generateSplits(Expense expense, String[] users){
        int amount = expense.getAmount();
        int splitAmount = Math.round(amount / users.length);
        Map<String, Split> splits = new HashMap<>();
        for(String userName: users){
            User user = userService.findByUserName(userName);
            Split split = new Split(user, splitAmount);
            splits.put(user.getUserName(), split);
        }
        expense.setSplits(splits);
    }

    public void generateDues(Expense expense) {
        Map<String, Contribution> contributions = expense.getContributions();
        Map<String, Split> splits = expense.getSplits();
        List<Balance> positiveBalances = new ArrayList();
        List<Balance> negativeBalances = new ArrayList();
        for (Map.Entry<String, Split> entry : splits.entrySet()) {
            Contribution contribution = contributions.get(entry.getKey());
            if(contribution == null){
                Balance balance = new Balance(entry.getKey(), entry.getValue().getAmount(), entry.getValue().getUser());
                negativeBalances.add(balance);
            }
            else{
                int amount = contribution.getAmount() - entry.getValue().getAmount();
                if(amount > 0){
                    Balance balance = new Balance(entry.getKey(), amount, entry.getValue().getUser());
                    positiveBalances.add(balance);
                }
                else{
                    Balance balance = new Balance(entry.getKey(), Math.abs(amount), entry.getValue().getUser());
                    negativeBalances.add(balance);
                }
            }
        }
        List<ExpenseDue> dues = calculateDues(positiveBalances, negativeBalances);
        expense.setDues(dues);
        System.out.println("SUCCESS");
    }

    public List<ExpenseDue> calculateDues(List<Balance> positiveBalances, List<Balance> negativeBalances){
        List<ExpenseDue> dues = new ArrayList();
        int p = 0;
        int n = 0;
        while(p < positiveBalances.size() && n < negativeBalances.size()){
            Balance positiveUser = positiveBalances.get(p);
            int positiveAmount = positiveUser.getAmount();
            Balance negativeUser = negativeBalances.get(n);
            int negativeAmount = negativeUser.getAmount();
            if(positiveAmount == 0){
                p++;
            }
            else if(negativeAmount == 0){
                n++;
            }
            else{
                int balance = positiveAmount - negativeAmount;
                if(balance >= 0){
                    ExpenseDue expenseDue = new ExpenseDue(negativeUser.getUser(), positiveUser.getUser(), negativeAmount);
                    positiveUser.setAmount(balance);
                    n++;
                    dues.add(expenseDue);
                }
                else if(balance < 0){
                    ExpenseDue expenseDue = new ExpenseDue(negativeUser.getUser(), positiveUser.getUser(), positiveAmount);
                    negativeUser.setAmount(Math.abs(balance));
                    p++;
                    dues.add(expenseDue);
                }
            }
        }
        return dues;
    }

    public void simplifyDues(Expense expense) {
        for(ExpenseDue due: expense.getDues()){
            String lenderName = due.getLender().getUserName();
            String borrowerName = due.getBorrower().getUserName();
            debtGraph.addDebt(borrowerName, lenderName, due.getAmount());
        }
        debtGraph.simplifyGraph();
    }
}
