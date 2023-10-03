package src.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLogger {
    private String logFileName;
    private SimpleDateFormat dateFormat;
    String userID = UniqueIdentifierGenerator.generateUniqueIdentifier();

    public CustomLogger(String logFileName) {
        this.logFileName = logFileName;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = " [" + timestamp + "]:   " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            writer.println(userID+ logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
