package com.example.geektrust.exception;
import java.lang.RuntimeException;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }
}
