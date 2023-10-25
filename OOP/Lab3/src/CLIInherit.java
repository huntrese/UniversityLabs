import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CLIInherit {
    private static String currentDirectory = System.getProperty("user.dir");
    private static String directoryPath = ".gip"; // Replace with the desired directory name
    private static String objPath = directoryPath+"/objects";
    private static String refsPath = directoryPath+"/refs";
    private static String headPath = refsPath+"/HEAD/";
    public static void main(String[] args) {
        try {

            if (args.length == 0) {
                System.out.println("Usage: gat [options]");
                return;
            }
            String arg = args[0];
            switch (arg) {

                case "init":
                    if (!Files.exists(Paths.get(directoryPath))) {

                        Files.createDirectories(Paths.get(objPath));
                        Files.createDirectories(Paths.get(headPath));
                        Ref master = new Ref("master", "");
                        System.out.println(master.getName());


                    } else{
                        System.out.println(".gip directory already exists");
                    }
                    break;
                case "commit":
                    File directory = new File(currentDirectory);
                    File[] files = directory.listFiles();
                    for (File file: files) {
                        if (!file.getName().equals(".gat") && !file.getName().equals(".gip")){
//                            System.out.println(file.getName()+file.getPath());
                            Blob blob = new Blob(file.getName(),file.getPath());
                            blob.createHashDir(objPath);

                        }

                    }

                    break;
                case "fetch":

                    break;
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

