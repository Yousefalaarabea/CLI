package org.example;

import java.util.Arrays;

class Parser {
    private String commandName;
    private String[] args;

    public boolean parse(String input) {
        String[] parts = input.trim().split(" ");
        if (parts.length < 1) {
            return false;
        }
        commandName = parts[0];
        args = Arrays.copyOfRange(parts, 1, parts.length);
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}