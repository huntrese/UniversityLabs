package gitObjects;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tree extends GitObject {
    private String content;

    public Tree(String path) {


        super(path);
        this.content = "";



    }

    @Override
    public String getType() {
        return "tree";
    }

    public String getContent() {
        return content;
    }

    @Override
    public String calculateSHA1(){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            String data= getContent();
            byte[] hash = messageDigest.digest(data.getBytes());
            StringBuilder hexString = gitHelper.getString(hash);
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public byte[] readFileBytes() {
        return gitHelper.readFileBytes(getPath());
    }

    @Override
    public String createHashDir(String objPath){
        try {
            String sha1Hash = calculateSHA1();
            Path fileHashPath = gitHelper.createHashDirHelper(objPath,sha1Hash);
            String data = getContent();
            Files.write(fileHashPath, String.format("%s %s\n%s",getType(),data.getBytes().length,data).getBytes());
            return sha1Hash;

        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addBlob(String hash, String name, Long modifiedTime ){
        content+=String.format("%s %s %s\n",hash,name,modifiedTime);
    }
}


