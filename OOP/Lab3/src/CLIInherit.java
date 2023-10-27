import fileTypes.ImageFiles;
import fileTypes.ProgramFiles;
import fileTypes.TextFiles;
import gitObjects.Blob;
import gitObjects.Ref;
import gitObjects.Tree;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
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

                        List<String> lines = Files.readAllLines(getObj(head));

                        for (String line : lines.subList(1, lines.size())) {
                            String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter

                            previousHash.add(splitLine[0]);
                            previousFiles.add(String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1)));
                            previousModified.add(Long.parseLong(splitLine[splitLine.length - 1]));


                        }
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
                                    tree.addBlob(previousHash.remove(index), previousFiles.remove(index), previousModified.remove(index));

                                } else{
                                    modified+=1;
                                    Blob blob = new Blob(file.getName(),file.getPath());
                                    blob.createHashDir(objPath);

                                    tree.addBlob(blob.getHash(), blob.getName(), blob.getModifiedTime());

                                }

                            } else {
                                modified+=1;
                                Blob blob = new Blob(file.getName(), file.getPath());
                                blob.createHashDir(objPath);

                                tree.addBlob(blob.getHash(), blob.getName(), blob.getModifiedTime());
                            }
                        }
                    }

                    modified+=previousFiles.size();

                    if(modified!=0) {
                        String treeHash = tree.createHashDir(objPath);
                        master.createBranch(treeHash);

                    }else {
                        System.out.println("Nothing to commit");
                    }
                    break;
                case "fetch":
                    try {


                        if(master !=null && Files.exists(Paths.get(master.getPath()))) {
                            String head = new String(master.readReference(), StandardCharsets.UTF_8);

                            List<String> lines = Files.readAllLines(getObj(head));
                            for (String line : lines.subList(1, lines.size())) {
                                String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                                String fileName = String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1));
                                String contentHash = splitLine[0];
                                Path filePath = getObj(contentHash);
                                byte[] fileContent = Files.readAllBytes(filePath);
                                // Create the file in the user's current directory
                                createFileInCurrentDirectory(fileName, fileContent);
                        }



                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "status":
                    File statusDirectory = new File(currentDirectory);
                    File[] statusFiles = statusDirectory.listFiles();
                    List<String> statusNames = new ArrayList<>();
                    List<Long> statusModified = new ArrayList<>();


                    if(Files.exists(Paths.get(master.getPath()))) {
                        String head = new String(master.readReference(), "UTF-8");

                        List<String> lines = Files.readAllLines(getObj(head));

                        for (String line : lines.subList(1, lines.size())) {
                            String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter

                            statusNames.add(String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1)));
                            statusModified.add(Long.parseLong(splitLine[splitLine.length - 1]));


                        }
                    } else {
                        System.out.println("No Commits Found");
                        break;
                    }
                    for (File file: statusFiles) {
                        if (!file.isDirectory()) {
                            if (statusNames.contains(file.getName())) {
                                int index= statusNames.indexOf(file.getName());

                                BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(file.getPath()), BasicFileAttributeView.class);
                                BasicFileAttributes basicAttributes = null;
                                basicAttributes = attributes.readAttributes();
                                long modificationTime = basicAttributes.lastModifiedTime().toMillis();

                                if (statusModified.get(index)==modificationTime){
                                    System.out.println(file.getName()+" No changes");

                                } else{

                                    System.out.println(file.getName() + " Changed");
                                }
                                statusNames.remove(index);
                                statusModified.remove(index);

                            } else {
                                System.out.println(file.getName()+" Added");

                            }
                        }
                    }
                    for (String fileName:statusNames) {
                        System.out.println(fileName+ " Removed");

                    }
                    break;
                case "find":

                    getObj(args[1]);

                    break;
                case "info":
                    String[] fileInfo = args[1].split("\\.");
                    switch (fileInfo[1]){
                        case "py", "java"->{
                            ProgramFiles programFiles = new ProgramFiles(fileInfo);

                        }
                        case  "txt","doc","docx"->{
                            TextFiles textFiles = new TextFiles(fileInfo);

                        }
                        case  "png","jpeg","jpg","jfif","webp"->{
                            ImageFiles imageFiles = new ImageFiles(fileInfo);

                        }
                    }

                    break;
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static Path getObj(String findObj) {

        File objects = new File(objPath);
        for (File file: objects.listFiles()){
            if(findObj.substring(0,2).equals(file.getName())){
                for(File object : file.listFiles()){
                    if(findObj.substring(2,findObj.length()).equals(object.getName())){
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

