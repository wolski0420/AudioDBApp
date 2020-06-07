package edu.agh;

import java.util.List;
import java.util.Scanner;

public class CmdReceiver {
    private final Scanner scanner;
    private final List<String> possibleCmds;
    public CmdReceiver(List<String> possibleCmds) {
        scanner=new Scanner(System.in);
        this.possibleCmds=possibleCmds;
    }

    String receiveCommand()
    {
        System.out.println("\nPossible commands: ");
        possibleCmds.forEach(System.out::println);
        System.out.println("\nProvide instructions: ");
        return scanner.nextLine();
    }
}
