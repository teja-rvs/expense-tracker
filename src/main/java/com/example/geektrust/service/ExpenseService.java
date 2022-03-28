package com.example.geektrust.service;

import com.example.geektrust.model.*;
import com.example.geektrust.repository.ExpenseRepository;

public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final DebtGraph debtGraph;

    public ExpenseService(){
        this.expenseRepository = ExpenseRepository.getInstance();
        this.debtGraph = DebtGraph.getInstance();
    }

    public void addExpense(Expense expense) {
        expenseRepository.create(expense);
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
