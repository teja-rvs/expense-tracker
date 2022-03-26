package com.example.geektrust.exception;

import java.lang.RuntimeException;

public class UserLimitException extends RuntimeException {
    public UserLimitException(String message){
        super(message);
    }
}
