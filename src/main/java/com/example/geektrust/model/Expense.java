package com.example.geektrust.model;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Expense {
    private String id;
    private int amount;
    private Map<String, Contribution> contributions;
    private Map<String, Split> splits;
    private List<ExpenseDue> dues;

    public Expense(int amount){
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.contributions = new HashMap();
        this.splits = new HashMap();
        this.dues = new ArrayList();
    }

    public void setSplits(Map<String, Split> splits) {
        this.splits = splits;
    }

    public void setContributions(Map<String, Contribution> contributions) {
        this.contributions = contributions;
    }

    public void setDues(List<ExpenseDue> dues) {
        this.dues = dues;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Map<String, Contribution> getContributions() {
        return contributions;
    }

    public List<ExpenseDue> getDues() {
        return dues;
    }

    public Map<String, Split> getSplits() {
        return splits;
    }
}
