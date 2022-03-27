package com.example.geektrust.repository;

import com.example.geektrust.model.TotalDue;
import com.example.geektrust.model.UserAmount;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TotalDueRepository {
    private static TotalDueRepository instance;

    private TotalDueRepository(){}

    public static TotalDueRepository getInstance(){
        if(instance == null){
            instance = new TotalDueRepository();
        }
        return instance;
    }


    Map<String, Map<String, Integer>> finalDues = new HashMap<>();

    public void addUser(String userName, Map<String, Integer> dues){
        finalDues.put(userName, dues);
    }

    public Map<String, Map<String, Integer>> getFinalDues() {
        return finalDues;
    }


}
