import java.io.IOException;

public class Scheduler {
    public static void main(String[] args) {
        while (true) {
            Commands.handleTrack();
        }
    }
    public static void handleScheduler(){
        try {
            // Define the classpath to the Scheduler class
            String classpath = System.getProperty("java.class.path");
            String className = "Scheduler"; // Make sure this is the correct class name
            // Create a ProcessBuilder to run the Scheduler class
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "/wait ","java", "-cp", classpath, className);

            // Start the process
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
