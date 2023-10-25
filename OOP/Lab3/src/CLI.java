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

public class CLI {
    private static String directoryPath = ".gat"; // Replace with the desired directory name
    private static String objPath = directoryPath+"/objects";
    private static String refsPath = directoryPath+"/refs";
    private static String headPath = refsPath+"/HEAD/";
    public static void main(String[] args) {


        if (args.length == 0) {
            System.out.println("Usage: gat [options]");
            return;
        }
        String arg = args[0];
            switch (arg) {

                case "init":

                    Path path = Paths.get(directoryPath);

                    if (!Files.exists(path)) {
                        try {
                            Files.createDirectory(path);
                            Files.createDirectory(Paths.get(objPath));
                            Files.createDirectory(Paths.get(refsPath));
                            Files.createDirectory(Paths.get(headPath));
                            Files.createFile(Paths.get(headPath+"/master"));

                            System.out.println("Directory created successfully.");
                        } catch (IOException e) {
                            System.err.println("Failed to create directory: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Directory already exists.");
                    }
                    break;
                case "commit":
                    try {
                        String currentDirectory = System.getProperty("user.dir");
                        File directory = new File(currentDirectory);
                        File[] files = directory.listFiles();
                        String treeHash = "";
                        File headDir = new File(headPath);
                        List<String> previousFiles = new ArrayList<>();
                        List<Long> previousModified = new ArrayList<>();
                        List<String> previousHash = new ArrayList<>();
                        File head=new File(headPath+"/master");
                        int modified = 0;

                        try {

                            File oldHead = headDir.listFiles()[0];
                            head=oldHead;
                            System.out.println(oldHead.getPath());

                            List<String> lines = Files.readAllLines(getObj(oldHead.getName()));
                            System.out.println(lines);

                            for (String line : lines.subList(1, lines.size())) {
                                String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                                System.out.println(splitLine[1] + " " + splitLine[2]);

                                previousHash.add(splitLine[0]);
                                previousFiles.add(splitLine[1]);
                                previousModified.add(Long.parseLong(splitLine[2]));

                            }
                        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(file.getPath()), BasicFileAttributeView.class);
                                    BasicFileAttributes basicAttributes = attributes.readAttributes();
                                    long modificationTime = basicAttributes.lastModifiedTime().toMillis();

                                    if (previousFiles.contains(file.getName())) {
                                        int index = previousFiles.indexOf(file.getName());
                                        Long previousFileModified = previousModified.get(index);

                                        if (modificationTime != previousFileModified) {
                                            modified += 1;
                                            System.out.println("Modified " + file.getName());



                                            treeHash = writeFileHashTree(treeHash, file, modificationTime);

                                        } else {

                                            treeHash += String.format("%s %s %s\n", previousHash.get(index), previousFiles.get(index), previousModified.get(index));

                                        }
                                    } else {
                                        modified+=1;
                                        treeHash = writeFileHashTree(treeHash, file, modificationTime);
                                    }
                                }
                            }
                        }
                        if (modified > 0) {
                            Files.delete(Paths.get(head.getPath()));

                            treeHash = "tree" + treeHash.getBytes().length + "\n" + treeHash;
                            System.out.println(treeHash);
                            String treeHashDir = createHashDir(treeHash, treeHash.getBytes(), "tree");

                            Files.createFile(Paths.get(headPath + treeHashDir));
                            //                    Files.write(Paths.get(headPath+treeHashDir),treeHashDir.);
                        } else System.out.println("No changes to commit");
                    }
                    catch (IOException  e) {
                        e.printStackTrace();
                    }

                    break;
                case "fetch":
                    try {
                        File headDir = new File(headPath );
                        File head = headDir.listFiles()[0];
                        List<String> lines = Files.readAllLines(getObj(head.getName()));

                        for (String line : lines.subList(1, lines.size())) {
                            String[] splitLine = line.split("\\s+"); // Use "\\s+" as the delimiter
                            String fileName = splitLine[1];
                            String contentHash = splitLine[0];
                            byte[] fileContent = readFileBytes(objPath + File.separator + contentHash.substring(0, 2) + File.separator + contentHash.substring(2));

                            // Create the file in the user's current directory
                            createFileInCurrentDirectory(fileName, fileContent);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "find":
                    getObj(args[1]);
                    break;
                default:
                    System.err.println("Unknown option: " + arg);
                    break;
            }

    }
    private static void createFileInCurrentDirectory(String fileName, byte[] content) {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, fileName);

        try {
            Files.write(filePath, content);
            System.out.println("File '" + fileName + "' created in current directory.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String writeFileHashTree(String treeHash, File file, long modificationTime) throws IOException {
        byte[] fileContent = readFileBytes(file.getPath());
        String commitString = "blob " + fileContent.length + " " + fileContent;

        String sha1Hash = createHashDir(commitString, fileContent, file.getName());


        System.out.println("Last Modified: " + modificationTime);
        treeHash += String.format("%s %s %s\n", sha1Hash, file.getName(), modificationTime);
        return treeHash;
    }

    private  static String createHashDir(String content,byte[] fileContent,String fileName){
        try {


            String sha1Hash = calculateSHA1(content.getBytes());
            System.out.println("SHA-1 hash: " + sha1Hash + " " + fileName);
            String fileHashDir = objPath + "/" + sha1Hash.substring(0, 2);
            Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2, sha1Hash.length()));
            if(!Files.exists(Paths.get(fileHashDir))) {
                Files.createDirectory(Paths.get(fileHashDir));
            }
            Files.createFile(fileHashPath);
            Files.write(fileHashPath, fileContent);
            return sha1Hash;

        }   catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
    }
    private static byte[] readFileBytes(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[1024];

            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            return buffer.toByteArray();
        }
    }

    private static String calculateSHA1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] hash = messageDigest.digest(data);

        // Convert the hash bytes to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hash) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
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

