import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CLI {

    public static void main(String[] args) {
        String directoryPath = ".gat"; // Replace with the desired directory name
        String objPath = directoryPath+"/objects";

        if (args.length == 0) {
            System.out.println("Usage: gat [options]");
            return;
        }
        for (String arg : args) {
            System.out.println(arg+arg);
            switch (arg) {

                case "init":

                    Path path = Paths.get(directoryPath);

                    if (!Files.exists(path)) {
                        try {
                            Files.createDirectory(path);
                            Files.createDirectory(Paths.get(objPath));
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
                    String treeHash="";
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {

                                    byte[] fileContent = readFileBytes(file.getPath());
                                    String commitString = "blob " + fileContent.length + " " + fileContent;

                                    String sha1Hash = calculateSHA1(commitString.getBytes());
                                    System.out.println("SHA-1 hash: " + sha1Hash + " " + file.getName());
                                    String fileHashDir = objPath+"/"+sha1Hash.substring(0,2);
                                    Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2,sha1Hash.length()));
                                    Files.createDirectory(Paths.get(fileHashDir));
                                    Files.createFile(fileHashPath);
                                    Files.write(fileHashPath,fileContent);

                                    treeHash+=String.format("%s %s\n",sha1Hash,file.getName());


                            }
                        }
                    }


                    System.out.println(treeHash);
                    String sha1Hash = calculateSHA1(treeHash.getBytes());
                    String fileHashDir = objPath+"/"+sha1Hash.substring(0,2);
                    Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2,sha1Hash.length()));
                    Files.createDirectory(Paths.get(fileHashDir));
                    Files.createFile(fileHashPath);
                    Files.write(fileHashPath,treeHash.getBytes());
                    } catch (IOException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    break;
                case "fetch":
//                    String currentDirectory = objPath
                    File objects = new File(objPath);
                    File[] objDirs = objects.listFiles();
                    if (objDirs != null) {
                        for (File objDir : objDirs) {
                            for(File objHash : objDir.listFiles()) {
                                if (objHash.isFile()) {
                                    try {
                                        byte[] fileContent = readFileBytes(objHash.getPath());
                                        String content = new String(fileContent,"UTF-8");
                                        System.out.println(content);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown option: " + arg);
                    break;
            }
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
}

