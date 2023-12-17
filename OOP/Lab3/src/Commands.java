import fileTypes.ImageFiles;
import fileTypes.ProgramFiles;
import fileTypes.TextFiles;
import gitObjects.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Commands {
    private static final String currentDirectory = System.getProperty("user.dir");
    private static final String directoryPath = ".gip"; // Replace with the desired directory name
    private static final String objPath = directoryPath + "/objects";
    private static final String refsPath = directoryPath + "/refs";

    public static void handleInit() {
        try {
            if (!Files.exists(Paths.get(directoryPath))) {
                Files.createDirectories(Paths.get(objPath));
                Files.createDirectories(Paths.get(refsPath));
                Ref HEAD = new Ref("HEAD");
                HEAD.initialize();

                System.out.println(".gip directory created");
            } else {
                System.out.println(".gip directory already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Tree handleCommit(Boolean isTrack) {
        File directory = new File(currentDirectory);
        File[] files = directory.listFiles();
        Tree tree = new Tree(objPath);
        List<String> previousFiles = new ArrayList<>();
        List<Long> previousModified = new ArrayList<>();
        List<String> previousHash = new ArrayList<>();
        int modified = 0;

        // Read previous commit's information
        int succesfullCommitRead = readPreviousCommit(previousFiles, previousModified, previousHash);
        if (succesfullCommitRead==1){return tree;}
        for (File file : files) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                int index = previousFiles.indexOf(fileName);
                if (index != -1 && previousModified.get(index) == file.lastModified()) {
                    // File hasn't changed
                    tree.addBlob(previousHash.remove(index), previousFiles.remove(index), previousModified.remove(index));
                } else {
                    // File is modified or new
                    modified++;
                    Blob blob = new Blob(fileName, file.getPath());
                    blob.createHashDir(objPath);
                    tree.addBlob(blob.getHash(), fileName, blob.getModifiedTime());


                }
            }
        }
        modified+=previousFiles.size();
        if (modified > 0) {
            String treeHash = tree.createHashDir(objPath);
            Ref HEAD = new Ref("HEAD");
            Branch branch = HEAD.getBranch();
            branch.createBranch(treeHash);
            System.out.println("Committed to: " + treeHash);


        } else if (!isTrack) {
            System.out.println("Nothing to commit");
        }
        return tree;
    }
    public static Tree handleCommit(){
        Boolean isTrack = false;
        return handleCommit(isTrack);

    }

    public static void handleFetch(String head) {
        try {
            List<String> lines = Files.readAllLines(Objects.requireNonNull(getObj(head)));
            for (String line : lines.subList(1, lines.size())) {
                String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                String fileName = String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1));
                String contentHash = splitLine[0];
                Path filePath = getObj(contentHash);

                if (filePath != null) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    createFileInCurrentDirectory(fileName, fileContent);
                } else {
                    System.out.println("Could not find content for hash: " + contentHash);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void handleBranch(String name){
        Branch branch = new Branch(name);
        branch.createBranch(handleCommit(true).createHashDir(objPath));
        System.out.println("Branch created: " +name);
        handleCheckout(name);

    }
    public static void handleCheckout(String name){
        try {
            Ref HEAD = new Ref("HEAD");

            Boolean valid = false;
            String head = null;

            if (Files.exists(Paths.get(refsPath+"/"+name))) {
                //Branch Found
                valid=true;
                HEAD.changeHead(name);
                Branch branch = HEAD.getBranch();
                head = new String(branch.readReference(), StandardCharsets.UTF_8);
            }
            if (Files.exists(Paths.get(objPath+"/"+name.substring(0,2)+"/"+name.substring(2)))) {
                //Commit Found
                valid=true;
                head = name;
            }
            if (valid==false) {
                System.out.println("Couldnt Checkout, invalid name");
                return;
            }
            File directory = new File(currentDirectory);
            for (File file: directory.listFiles()){
                if (!file.getName().equals(directoryPath)){
                    Files.delete(Paths.get(file.getPath()));

                }
            }
            handleFetch(head);
            System.out.println("Checkout to ->"+name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleStatus(Boolean isTrack) {
        Ref HEAD = new Ref("HEAD");

        File statusDirectory = new File(currentDirectory);
        File[] statusFiles = statusDirectory.listFiles();
        List<String> statusNames = new ArrayList<>();
        List<Long> statusModified = new ArrayList<>();
        Branch branch = HEAD.getBranch();
        // Read previous commit's information
        readPreviousCommit(statusNames, statusModified, null);
        if (statusNames.isEmpty()) {
            System.out.println("No Commits Found");
            return;
        } else if (!isTrack) {
            System.out.println("Last snapshot at: "+new String(branch.readSnapTime(),StandardCharsets.UTF_8));
        }

        assert statusFiles != null;
        for (File file : statusFiles) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                int index = statusNames.indexOf(fileName);

                if (index != -1) {
                    long modificationTime = file.lastModified();

                    if (statusModified.get(index) == modificationTime) {
                        if (!isTrack) {
                            System.out.println(fileName + " No changes");
                        }
                    } else {
                        System.out.println(fileName + " Changed");
                    }

                    statusNames.remove(index);
                    statusModified.remove(index);
                } else {
                    System.out.println(fileName + " Added");
                }
            }
        }

        for (String fileName : statusNames) {
            System.out.println(fileName + " Removed");
        }
    }
    public static void handleStatus(){
        Boolean isTrack = false;
        handleStatus(isTrack);
    }

    public static void handleInfo(String args) {

        if (Files.exists(Paths.get(args))) {
            String[] fileInfo = args.split("\\.");
            switch (fileInfo[1]) {
                case "py", "java" -> new ProgramFiles(fileInfo);
                case "txt", "doc", "docx" -> new TextFiles(fileInfo);
                case "png", "jpeg", "jpg", "jfif", "webp" -> new ImageFiles(fileInfo);
                default -> System.out.println("Unsupported file type: " + fileInfo[1]);
            }
        }else {
            System.out.println("File not found: "+args);
        }
    }

    public static Path getObj(String findObj) {
        File objects = new File(objPath);
        for (File file : Objects.requireNonNull(objects.listFiles())) {
            if (findObj.substring(0, 2).equals(file.getName())) {
                for (File object : Objects.requireNonNull(file.listFiles())) {
                    if (findObj.substring(2).equals(object.getName())) {
                        return Paths.get(object.getPath());
                    }
                }
            }
        }
        return null;
    }

    private static int readPreviousCommit(List<String> files, List<Long> modifiedTimes, List<String> hashes ) {
        try {
            Ref HEAD = new Ref("HEAD");
            Branch branch = HEAD.getBranch();

            if(branch==null){
                System.out.println("HEAD is detached cannot commit");
                return 1;
            }

            if (new String(gitHelper.readFileBytes(branch.getPath())).equals("")){return 0;}
            String commit =new String(branch.readReference(),StandardCharsets.UTF_8);

            List<String> lines = Files.readAllLines(Objects.requireNonNull(getObj(commit)));
            for (String line : lines.subList(1, lines.size())) {
                String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                if (hashes != null) {
                    hashes.add(splitLine[0]);
                }
                files.add(String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1)));
                modifiedTimes.add(Long.parseLong(splitLine[splitLine.length - 1]));
            }

        }catch (IOException e){
            e.printStackTrace();
    }
        return 2;

    }

    private static void createFileInCurrentDirectory(String fileName, byte[] content) {
        Path filePath = Paths.get(currentDirectory, fileName);
        try {
            Files.write(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void handleTrack() {


        handleStatus(true);
        handleCommit(true);
        try {
            Thread.sleep(5000); // Sleep for 5 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
