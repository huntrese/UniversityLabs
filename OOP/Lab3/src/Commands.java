import fileTypes.ImageFiles;
import fileTypes.ProgramFiles;
import fileTypes.TextFiles;
import gitObjects.Blob;
import gitObjects.Ref;
import gitObjects.Tree;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Commands {
    private static final String currentDirectory = System.getProperty("user.dir");
    private static final String directoryPath = ".gip"; // Replace with the desired directory name
    private static final String objPath = directoryPath + "/objects";
    private static final String refsPath = directoryPath + "/refs";
    private static final Ref master = new Ref("master");

    public static void handleInit() {
        try {
            if (!Files.exists(Paths.get(directoryPath))) {
                Files.createDirectories(Paths.get(objPath));
                String headPath = refsPath + "/HEAD/";
                Files.createDirectories(Paths.get(headPath));
                System.out.println(".gip directory created");
            } else {
                System.out.println(".gip directory already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleCommit() {
        File directory = new File(currentDirectory);
        File[] files = directory.listFiles();
        Tree tree = new Tree(objPath);
        List<String> previousFiles = new ArrayList<>();
        List<Long> previousModified = new ArrayList<>();
        List<String> previousHash = new ArrayList<>();
        int modified = 0;

        // Read previous commit's information
        readPreviousCommit(previousFiles, previousModified, previousHash);
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
            master.createBranch(treeHash);
            System.out.println("Committed to: " + treeHash);
        } else {
            System.out.println("Nothing to commit");
        }
    }

    public static void handleFetch() {
        try {
            if (!Files.exists(Paths.get(master.path()))) {
                System.out.println("No Commits Found");
                return;
            }

            String head = new String(master.readReference(), StandardCharsets.UTF_8);
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

    public static void handleStatus() {
        File statusDirectory = new File(currentDirectory);
        File[] statusFiles = statusDirectory.listFiles();
        List<String> statusNames = new ArrayList<>();
        List<Long> statusModified = new ArrayList<>();

        // Read previous commit's information
        readPreviousCommit(statusNames, statusModified, null);

        if (statusNames.isEmpty()) {
            System.out.println("No Commits Found");
            return;
        }

        assert statusFiles != null;
        for (File file : statusFiles) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                int index = statusNames.indexOf(fileName);

                if (index != -1) {
                    long modificationTime = file.lastModified();

                    if (statusModified.get(index) == modificationTime) {
                        System.out.println(fileName + " No changes");
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

    public static void handleInfo(String args) {
        String[] fileInfo = args.split("\\.");
        switch (fileInfo[1]) {
            case "py", "java" -> new ProgramFiles(fileInfo);
            case "txt", "doc", "docx" -> new TextFiles(fileInfo);
            case "png", "jpeg", "jpg", "jfif", "webp" -> new ImageFiles(fileInfo);
            default -> System.out.println("Unsupported file type: " + fileInfo[1]);
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

    private static void readPreviousCommit(List<String> files, List<Long> modifiedTimes, List<String> hashes) {
        try {
            if (Files.exists(Paths.get(master.path()))) {
                String head = new String(master.readReference(), StandardCharsets.UTF_8);
                List<String> lines = Files.readAllLines(Objects.requireNonNull(getObj(head)));
                for (String line : lines.subList(1, lines.size())) {
                    String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                    if (hashes != null) {
                        hashes.add(splitLine[0]);
                    }
                    files.add(String.join(" ", Arrays.copyOfRange(splitLine, 1, splitLine.length - 1)));
                    modifiedTimes.add(Long.parseLong(splitLine[splitLine.length - 1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
