package com.example.geektrust.model;

public class ExpenseDue extends Due{
    public ExpenseDue(User borrower, User lender, int amount) {
        super(borrower, lender, amount);
    }
}
