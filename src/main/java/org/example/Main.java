package org.example;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            if (terminal.parser.parse(input)) {
                String commandName = terminal.parser.getCommandName();
                String[] cmdArgs = terminal.parser.getArgs();
                terminal.chooseCommandAction(commandName, cmdArgs);
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        scanner.close();
    }
}