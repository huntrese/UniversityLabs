package gitObjects;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Blob extends GitObject {
    private final byte[] content;
    private final String name;
    private Long modifiedTime;
    private String hash;
    public Blob(String name,String path) {
        super(path);
        this.name=name;
        this.content = readFileBytes();
        setModifiedTime();
    }

    public String getHash() {
        return hash;
    }

    public void setModifiedTime() {
        try {
            BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(getPath()), BasicFileAttributeView.class);
            BasicFileAttributes basicAttributes = attributes.readAttributes();
            this.modifiedTime = basicAttributes.lastModifiedTime().toMillis();
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
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] data= getContent();
            byte[] hash = messageDigest.digest(String.format("%s %s %s",getType(),data.length, Arrays.toString(data)).getBytes());
            StringBuilder hexString = gitHelper.getString(hash);
            this.hash=hexString.toString();
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] readFileBytes() {
        return gitHelper.readFileBytes(getName());
    }

    @Override
    public String createHashDir(String objPath){
        try {
            String sha1Hash = calculateSHA1();
            Path fileHashPath = gitHelper.createHashDirHelper(objPath,sha1Hash);
            Files.write(fileHashPath, new String(getContent(), StandardCharsets.UTF_8).getBytes());
            return sha1Hash;

        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


