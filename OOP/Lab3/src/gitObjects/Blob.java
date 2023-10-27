package gitObjects;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob extends GitObject {
    private byte[] content;
    private String name;
    private Long modifiedTime;
    private String hash;
    public Blob(String name,String path) {


        super(path);
//        System.out.println("created obj");
        this.content = readFileBytes();
        this.name=name;
        setModifiedTime();
//        System.out.println("added content");


    }

    public void setModifiedTime() {
        try {
            BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(getPath()), BasicFileAttributeView.class);
            BasicFileAttributes basicAttributes = null;
            basicAttributes = attributes.readAttributes();

            long modificationTime = basicAttributes.lastModifiedTime().toMillis();

            this.modifiedTime = modificationTime;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Long getModifiedTime() {
        return modifiedTime;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
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
        this.hash= hexString.toString();
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
            String fileHashDir = objPath + "/" + sha1Hash.substring(0, 2);
            Path fileHashDirPath = Paths.get(fileHashDir);
            Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2, sha1Hash.length()));
            if(!Files.exists(fileHashDirPath)) {
                Files.createDirectory(fileHashDirPath);
            }else{
                Files.delete(fileHashPath);
            }
            Files.createFile(fileHashPath);
            Files.write(fileHashPath, getContent());
            return sha1Hash;

        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


