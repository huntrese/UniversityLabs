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
    private static final String currentDirectory = System.getProperty("user.dir");
    private static final String directoryPath = ".gip"; // Replace with the desired directory name
    private static final String objPath = directoryPath+"/objects";
    private static final String refsPath = directoryPath+"/refs";
    private static final Ref master = new Ref("master");

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
                        String headPath = refsPath + "/HEAD/";
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
                    List<String> previousFiles = new ArrayList<>();
                    List<Long> previousModified = new ArrayList<>();
                    List<String> previousHash = new ArrayList<>();
                    int modified = 0;

                    if(Files.exists(Paths.get(master.getPath()))) {
                        String head = new String(master.readReference(), StandardCharsets.UTF_8);
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
                        master.createBranch(tree.getPath() + "/" + treeHash);
                    }else {
                        System.out.println("Nothing to commit");
                    }
                    break;
                case "fetch":
                    try {


                        if(master !=null && Files.exists(Paths.get(master.getPath()))) {
                            String head = new String(master.readReference(), StandardCharsets.UTF_8);
                            System.out.println(head);

                            List<String> lines = Files.readAllLines(getObj(head));
                            for (String line : lines.subList(1, lines.size())) {
                                System.out.println(line);
                                String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                                String fileName = String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1));
                                String contentHash = splitLine[0];
                                System.out.println(fileName);
                                Path filePath = getObj(contentHash);
                                byte[] fileContent = Files.readAllBytes(filePath);
                                System.out.println(new String(fileContent, StandardCharsets.UTF_8).getBytes());
                                // Create the file in the user's current directory
                                createFileInCurrentDirectory(fileName, fileContent);
                        }



                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "find":
                    getObj(args[1]);
                    break;
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static Path getObj(String findObj) {

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


        return null;
    }
    private static void createFileInCurrentDirectory(String fileName, byte[] content) {
        Path filePath = Paths.get(currentDirectory, fileName);

        try {
            Files.write(filePath, content);
            System.out.println("File '" + fileName + "' created in current directory.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

