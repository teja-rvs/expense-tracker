package com.example.geektrust.model;

import java.util.Map;
import java.util.HashMap;

public enum Command {
    MOVE_IN("MOVE_IN"),
    SPEND("SPEND"),
    DUES("DUES"),
    CLEAR_DUE("CLEAR_DUE"),
    MOVE_OUT("MOVE_OUT");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String toString() {
        return this.command;
    }

    private static final HashMap<String, Command> map = new HashMap<>(values().length, 1);

    static {
        for (Command c : values()) map.put(c.command, c);
    }

    public static Command of(String command){
        return map.get(command);
    }
}
