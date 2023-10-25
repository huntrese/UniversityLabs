import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob extends GitObject {
    private byte[] content;

    public Blob(String name,String path) {


        super(name,path);
//        System.out.println("created obj");
        this.content = readFileBytes();
//        System.out.println("added content");

    }

    @Override
    public String getType() {
        return "blob";
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String calculateSHA1(){
        try {
        MessageDigest messageDigest = null;
        messageDigest = MessageDigest.getInstance("SHA-1");

        byte[] data= getContent();
        byte[] hash = messageDigest.digest(String.format("%s %s %s",getType(),data.length,data).getBytes());

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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] readFileBytes() {
//        System.out.println(getPath());
        try (InputStream inputStream = new FileInputStream(getPath())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[1024];
//            System.out.println("started reading");
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createHashDir(String objPath){
        try {


            String sha1Hash = calculateSHA1();
            System.out.println("SHA-1 hash: " + sha1Hash + " " + getName());
            String fileHashDir = objPath + "/" + sha1Hash.substring(0, 2);
            Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2, sha1Hash.length()));
            if(!Files.exists(Paths.get(fileHashDir))) {
                Files.createDirectory(Paths.get(fileHashDir));
            }
            Files.createFile(fileHashPath);
            Files.write(fileHashPath, getContent());
            return sha1Hash;

        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


