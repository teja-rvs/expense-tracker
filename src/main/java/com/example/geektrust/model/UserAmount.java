package com.example.geektrust.model;

public class UserAmount {
    private User user;
    private int amount;

    public UserAmount(User user, int amount){
        this.user = user;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }
}
