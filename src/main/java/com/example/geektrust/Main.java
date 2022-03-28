package com.example.geektrust;

import com.example.geektrust.model.Command;
import com.example.geektrust.model.Expense;
import com.example.geektrust.model.User;
import com.example.geektrust.service.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String file = args[0];
        UserService userService = new UserService();
        ExpenseService expenseService = new ExpenseService();
        TotalDueService totalDueService = new TotalDueService();
        ClearDueService clearDueService = new ClearDueService();
        MoveOutService moveOutService = new MoveOutService();
        try {
            Scanner scan = new Scanner(new File(file));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] commands = line.split(" ");
                switch (Command.of(commands[0])) {
                    case MOVE_IN:
                        String userName = commands[1];
                        userService.addUser(userName);
                        break;
                    case SPEND:
                        int amount = Integer.parseInt(commands[1]);
                        String[] userNames = Arrays.copyOfRange(commands, 2, commands.length);
                        userService.validUsers(userNames)
                                .ifPresentOrElse(
                                        users -> {
                                            String contributors = commands[2];
                                            Expense expense = new Expense
                                                    .ExpenseBuilder(amount)
                                                    .contributor(userService.findByUserName(contributors))
                                                    .generateSplits(users)
                                                    .generateDues()
                                                    .build();
                                            expenseService.addExpense(expense);
                                            expenseService.simplifyDues(expense);
                                            totalDueService.updateFinalDues();
                                        },
                                        () -> System.out.println("MEMBER_NOT_FOUND"));
                        break;
                    case DUES:
                        User user = userService.findByUserName(commands[1]);
                        totalDueService.getUserDues(user.getUserName());
                        break;
                    case CLEAR_DUE:
                        User payer = userService.findByUserName(commands[1]);
                        User payee = userService.findByUserName(commands[2]);
                        int payment = Integer.parseInt(commands[3]);
                        clearDueService.settle(payer, payee, payment * -1);
                        break;
                    case MOVE_OUT:
                        User mover = userService.findByUserName(commands[1]);
                        if (mover == null) {
                            System.out.println("MEMBER_NOT_FOUND");
                        } else {
                            moveOutService.move_out(mover);
                        }
                        break;
                    default:
                        System.out.println("INVALID COMMAND");
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

    }
}