import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CLI {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: gat [options]");
            return;
        }
        for (String arg : args) {
            System.out.println(arg+arg);
            switch (arg) {

                case "init":
                    String directoryPath = ".gat"; // Replace with the desired directory name

                    Path path = Paths.get(directoryPath);

                    if (!Files.exists(path)) {
                        try {
                            Files.createDirectory(path);
                            System.out.println("Directory created successfully.");
                        } catch (IOException e) {
                            System.err.println("Failed to create directory: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Directory already exists.");
                    }
                    break;
                case "commit":
                    String currentDirectory = System.getProperty("user.dir");
                    File directory = new File(currentDirectory);
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                try {
                                    byte[] fileContent = readFileBytes(file.getPath());
                                    String sha1Hash = calculateSHA1(fileContent);
                                    System.out.println("SHA-1 Hash: " + sha1Hash);
                                } catch (IOException | NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

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

