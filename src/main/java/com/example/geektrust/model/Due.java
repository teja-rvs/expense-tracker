package com.example.geektrust.model;

public class Due {
    private User borrower;
    private User lender;
    private int amount;

    public Due(User borrower, User lender, int amount){
        this.borrower = borrower;
        this.lender = lender;
        this.amount = amount;
    }

    public User getLender() {
        return lender;
    }

    public User getBorrower() {
        return borrower;
    }

    public int getAmount() {
        return amount;
    }
}
