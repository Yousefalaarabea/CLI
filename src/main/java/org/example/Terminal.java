package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
public class Terminal {
    Parser parser;
    private List<String> commandHistory;

    public Terminal() {
        parser = new Parser();
        commandHistory = new ArrayList<>();
    }

    public void echo(String[] args) {
        if (args.length >= 1) {
            System.out.println(args[0]);
        } else {
            System.out.println("Usage: echo <text>");
        }
    }

    public void pwd() {
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);
    }

    public void cd(String[] args) {
        if (args.length == 0) {
            // Change directory to home directory
            String homeDir = System.getProperty("user.home");
            System.setProperty("user.dir", homeDir);
        } else if (args[0].equals("..")) {
            // Move up one directory
            File currentDir = new File(System.getProperty("user.dir"));
            File parentDir = currentDir.getParentFile();
            if (parentDir != null) {
                System.setProperty("user.dir", parentDir.getAbsolutePath());
            }
        } else {
            // Change directory to the specified path
            File newDir = new File(args[0]);
            if (newDir.exists() && newDir.isDirectory()) {
                System.setProperty("user.dir", newDir.getAbsolutePath());
            } else {
                System.out.println("Invalid directory path.");
            }
        }
    }

    public void ls() {
        File currentDir = new File(System.getProperty("user.dir"));
        String[] contents = currentDir.list();
        if (contents != null) {
            Arrays.sort(contents);
            for (String item : contents) {
                System.out.println(item);
            }
        }
    }

    public void lsReverse() {
        File currentDir = new File(System.getProperty("user.dir"));
        String[] contents = currentDir.list();
        if (contents != null) {
            Arrays.sort(contents, (a, b) -> b.compareTo(a));
            for (String item : contents) {
                System.out.println(item);
            }
        }
    }

    public void mkdir(String[] args) {
        for (String arg : args) {
            File newDir = new File(arg);
            if (!newDir.exists()) {
                if (newDir.mkdirs()) {
                    System.out.println("Directory created: " + newDir.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + newDir.getAbsolutePath());
                }
            } else {
                System.out.println("Directory already exists: " + newDir.getAbsolutePath());
            }
        }
    }

    public void rmdir(String[] args) {
        for (String arg : args) {
            File dir = new File(arg);
            if (dir.exists() && dir.isDirectory()) {
                if (arg.equals("*")) {
                    // Remove all empty directories in the current directory
                    for (File subDir : dir.listFiles()) {
                        if (subDir.isDirectory() && subDir.list().length == 0) {
                            if (subDir.delete()) {
                                System.out.println("Directory removed: " + subDir.getAbsolutePath());
                            } else {
                                System.out.println("Failed to remove directory: " + subDir.getAbsolutePath());
                            }
                        }
                    }
                } else if (dir.list().length == 0) {
                    // Remove the directory if it's empty
                    if (dir.delete()) {
                        System.out.println("Directory removed: " + dir.getAbsolutePath());
                    } else {
                        System.out.println("Failed to remove directory: " + dir.getAbsolutePath());
                    }
                } else {
                    System.out.println("Directory is not empty: " + dir.getAbsolutePath());
                }
            } else {
                System.out.println("Directory does not exist: " + dir.getAbsolutePath());
            }
        }
    }

    public void touch(String[] args) {
        for (String arg : args) {
            File newFile = new File(arg);
            if (!newFile.exists()) {
                try {
                    if (newFile.createNewFile()) {
                        System.out.println("File created: " + newFile.getAbsolutePath());
                    } else {
                        System.out.println("Failed to create file: " + newFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.out.println("Error creating file: " + e.getMessage());
                }
            } else {
                System.out.println("File already exists: " + newFile.getAbsolutePath());
            }
        }
    }

    public void cp(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: cp <source_file> <destination_file>");
            return;
        }

        File sourceFile = new File(args[0]);
        File destFile = new File(args[1]);

        if (!sourceFile.exists() || !sourceFile.isFile()) {
            System.out.println("Source file does not exist or is not a regular file.");
            return;
        }

        try {
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied: " + sourceFile.getAbsolutePath() + " -> " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
    }

    public void cpR(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: cp -r <source_directory> <destination_directory>");
            return;
        }

        File sourceDir = new File(args[0]);
        File destDir = new File(args[1]);

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.out.println("Source directory does not exist or is not a directory.");
            return;
        }

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        if (!destDir.isDirectory()) {
            System.out.println("Destination is not a directory.");
            return;
        }

        File[] sourceFiles = sourceDir.listFiles();

        if (sourceFiles != null) {
            for (File sourceFile : sourceFiles) {
                if (sourceFile.isDirectory()) {
                    File newDestDir = new File(destDir, sourceFile.getName());
                    newDestDir.mkdirs();
                    cpR(new String[]{sourceFile.getAbsolutePath(), newDestDir.getAbsolutePath()});
                } else {
                    File destFile = new File(destDir, sourceFile.getName());
                    try {
                        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println("Error copying file: " + e.getMessage());
                    }
                }
            }
            System.out.println("Directory copied: " + sourceDir.getAbsolutePath() + " -> " + destDir.getAbsolutePath());
        }
    }

    public void rm(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: rm <file_name>");
            return;
        }

        File fileToRemove = new File(args[0]);
        if (fileToRemove.exists() && fileToRemove.isFile()) {
            if (fileToRemove.delete()) {
                System.out.println("File removed: " + fileToRemove.getAbsolutePath());
            } else {
                System.out.println("Failed to remove file: " + fileToRemove.getAbsolutePath());
            }
        } else {
            System.out.println("File does not exist or is not a regular file: " + fileToRemove.getAbsolutePath());
        }
    }

    public void cat(String[] args) {
        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))){
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else if (args.length == 2) {
            try (BufferedReader br1 = new BufferedReader(new FileReader(args[0]));
                 BufferedReader br2 = new BufferedReader(new FileReader(args[1]))) {

                String line;
                while ((line = br1.readLine()) != null) {
                    System.out.println(line);
                }

                while ((line = br2.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else {
            System.out.println("Usage: cat <file> OR cat <file1> <file2>");
        }
    }

    public void wc(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: wc <file>");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            int lines = 0;
            int words = 0;
            int characters = 0;
            String line;

            while ((line = br.readLine()) != null) {
                lines++;
                words += line.split("\\s+").length;
                characters += line.length();
            }

            System.out.println(lines + " " + words + " " + characters + " " + args[0]);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void redirectOutput(String[] args, boolean append) {
        if (args.length != 2) {
            System.out.println("Usage: > or >> <file_name>");
            return;
        }

        String commandOutput = args[0];
        String fileName = args[1];
        try {
            PrintStream fileStream = new PrintStream(new FileOutputStream(fileName, append));
            System.setOut(fileStream);

            // Execute the command whose output is to be redirected
            chooseCommandAction(commandOutput, new String[0]);

            // Reset the standard output
            System.setOut(System.out);
            fileStream.close();
        } catch (IOException e) {
            System.out.println("Error redirecting output to file: " + e.getMessage());
        }
    }

    public void history() {
        System.out.println("Command History:");
        int count = 1;
        for (String command : commandHistory) {
            System.out.println(count + " " + command);
            count++;
        }
    }

    public void chooseCommandAction(String commandName, String[] args) {
        switch (commandName) {
            case "echo":
                echo(args);
                break;
            case "pwd":
                pwd();
                break;
            case "cd":
                cd(args);
                break;
            case "ls":
                ls();
                break;
            case "ls -r":
                lsReverse();
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "rmdir":
                rmdir(args);
                break;
            case "touch":
                touch(args);
                break;
            case "cp":
                cp(args);
                break;
            case "cp -r":
                cpR(args);
                break;
            case "rm":
                rm(args);
                break;
            case "cat":
                cat(args);
                break;
            case "wc":
                wc(args);
                break;
            case ">":
                redirectOutput(args, false);
                break;
            case ">>":
                redirectOutput(args, true);
                break;
            case "history":
                history();
                break;
            default:
                System.out.println("Command not recognized.");
        }
        commandHistory.add(commandName + " " + String.join(" ", args));

    }

}
