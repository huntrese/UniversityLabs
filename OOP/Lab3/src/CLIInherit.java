
public class CLIInherit {

    public static void main(String[] args){

        if (args.length == 0) {
            System.out.println("Usage: gip [options]");
            return;
        }
        String arg = args[0];
        switch (arg) {
            case "init" -> Commands.handleInit();
            case "commit" -> Commands.handleCommit();
            case "branch" -> Commands.handleBranch(args[1]);
            case "checkout" -> Commands.handleCheckout(args[1]);
            case "status" -> Commands.handleStatus();
            case "find" -> Commands.getObj(args[1]);
            case "info" -> Commands.handleInfo(args[1]);
            case "track" -> Scheduler.handleScheduler();

        }
    }
}
