package com.example.geektrust.repository;

import com.example.geektrust.model.Expense;

import java.util.Map;
import java.util.HashMap;

public class ExpenseRepository {
    private static ExpenseRepository instance;

    private ExpenseRepository(){}

    public static ExpenseRepository getInstance(){
        if(instance == null){
            instance = new ExpenseRepository();
        }
        return instance;
    }

    private Map<String, Expense> expenseMap = new HashMap();

    public void create(Expense expense){
        expenseMap.put(expense.getId(), expense);
    }
}
