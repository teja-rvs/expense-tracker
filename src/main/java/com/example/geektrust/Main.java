package com.example.geektrust;

import com.example.geektrust.model.Command;
import com.example.geektrust.service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String file = args[0];
        UserService userService = new UserService();
        try{
            Scanner scan = new Scanner(new File(file));
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] commands = line.split(" ");
                switch (Command.of(commands[0])){
                    case MOVE_IN:
                        String userName = commands[1];
                        userService.addUser(userName);
                        break;
                    default:
                        System.out.println("INVALID COMMAND");
                }
            }
        }
        catch (FileNotFoundException ex){
            System.out.println("File Not Found");
        }

    }
}


//public static void readText throws FileNotFoundException {
//
//