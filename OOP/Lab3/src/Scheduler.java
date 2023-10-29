import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    public static void main(String[] args) {
        while (true) {
            Commands.handleTrack();
//            System.console().writer();
        }
    }
}
