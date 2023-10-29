import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class CLIInherit {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: gip [options]");
            return;
        }


        switch (args[0]) {
            case "init", "commit", "fetch", "status","info" -> executeCommand(args);
            case "track" -> {
                Runnable trackCommand = new Runnable() {
                    public void run() {
                        Commands.handleTrack();
                    }
                };
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.submit(trackCommand);
            }
        }

        // User input handling
        if (args[0].equals("track")) {
            handleUserInput();
        }
    }

    private static void executeCommand(String[] args) {
        String arg = args[0];
        switch (arg) {
            case "info" -> Commands.handleInfo(args[1]);
            case "init" -> Commands.handleInit();
            case "commit" -> Commands.handleCommit();
            case "fetch" -> Commands.handleFetch();
            case "status" -> Commands.handleStatus();
        }
    }

    private static void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine();
            executeCommand(userInput.split(" "));
        }
    }
}
