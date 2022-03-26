package com.example.geektrust.model;

public class Balance {
    private String userName;
    private int amount;
    private User user;

    public Balance(String userName, int amount, User user){
        this.userName = userName;
        this.amount = amount;
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public String getUserName() {
        return userName;
    }

    public User getUser() {
        return user;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
