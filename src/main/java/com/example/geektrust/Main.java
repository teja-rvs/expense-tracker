package com.example.geektrust;

import com.example.geektrust.model.Command;
import com.example.geektrust.model.Expense;
import com.example.geektrust.service.ExpenseService;
import com.example.geektrust.service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String file = args[0];
        UserService userService = new UserService();
        ExpenseService expenseService = new ExpenseService();
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
                    case SPEND:
                        int amount = Integer.parseInt(commands[1]);
                        String[] users = Arrays.copyOfRange(commands, 2, commands.length);
                        if (expenseService.validateExpenseUsers(users)){
                            Expense expense = expenseService.addExpense(amount);
                            String contributors = commands[2];
                            expenseService.addContributors(expense, contributors);
                            expenseService.generateSplits(expense, users);
                            expenseService.generateDues(expense);
                            expenseService.print(expense);
                        }
                        else{
                            System.out.println("MEMBER_NOT_FOUND");
                        }
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