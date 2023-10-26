import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
                System.out.println("Usage: gip [options]");
                return;
            }
            String arg = args[0];
            switch (arg) {

                case "init":
                    if (!Files.exists(Paths.get(directoryPath))) {

                        Files.createDirectories(Paths.get(objPath));
                        Files.createDirectories(Paths.get(headPath));
//                        Ref master = new Ref("master", "");
//                        System.out.println(master.getPath());


                    } else{
                        System.out.println(".gip directory already exists");
                    }
                    break;
                case "commit":
                    File directory = new File(currentDirectory);
                    File[] files = directory.listFiles();
                    Tree tree = new Tree(objPath);
                    Ref master = new Ref("master", "");
                    List<String> previousFiles = new ArrayList<>();
                    List<Long> previousModified = new ArrayList<>();
                    List<String> previousHash = new ArrayList<>();
                    int modified = 0;

                    if(Files.exists(Paths.get(master.getPath()))) {
                        String head = new String(master.readFileBytes(), StandardCharsets.UTF_8);
                        System.out.println(head);

                        List<String> lines = Files.readAllLines(getObj(head));
                        System.out.println(lines);

                        for (String line : lines.subList(1, lines.size())) {
                            String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                            System.out.println(splitLine[1] + " " + splitLine[2]);

                            previousHash.add(splitLine[0]);
                            previousFiles.add(String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1)));
                            previousModified.add(Long.parseLong(splitLine[splitLine.length - 1]));


                        }
                        System.out.println(previousHash);
                        System.out.println(previousFiles);
                        System.out.println(previousModified);
                    }
                    for (File file: files) {
                        if (!file.isDirectory()) {
                            if (previousFiles.contains(file.getName())) {
                                int index= previousFiles.indexOf(file.getName());

                                BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(file.getPath()), BasicFileAttributeView.class);
                                BasicFileAttributes basicAttributes = null;
                                basicAttributes = attributes.readAttributes();
                                long modificationTime = basicAttributes.lastModifiedTime().toMillis();

                                if (previousModified.get(index)==modificationTime){
                                    System.out.println("same");
                                    tree.addBlob(previousHash.get(index), previousFiles.get(index), previousModified.get(index));

                                } else{
                                    modified+=1;
                                    Blob blob = new Blob(file.getName(),file.getPath());
                                    blob.createHashDir(objPath);

                                    tree.addBlob(blob.getHash(), blob.getName(), blob.getModifiedTime());

                                }

                            } else {
//                            System.out.println(file.getName()+file.getPath());
                                modified+=1;
                                Blob blob = new Blob(file.getName(), file.getPath());
                                blob.createHashDir(objPath);

                                tree.addBlob(blob.getHash(), blob.getName(), blob.getModifiedTime());
                            }
                        }
                    }
                    if(modified!=0) {
                        String treeHash = tree.createHashDir(objPath);
                        System.out.println("ss" + tree.getPath() + treeHash);
                        master.createHashDir(tree.getPath() + "/" + treeHash);
                    }else {
                        System.out.println("Nothing to commit");
                    }
                    break;
                case "fetch":

                    break;
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static Path getObj(String findObj) {
        try {
            System.out.println(findObj);
            File objects = new File(objPath);
            for (File file: objects.listFiles()){
                System.out.println(file.getName());
                if(findObj.substring(0,2).equals(file.getName())){
                    for(File object : file.listFiles()){
                        if(findObj.substring(2,findObj.length()).equals(object.getName())){
                            System.out.println("found");
                            return Paths.get(object.getPath());
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

