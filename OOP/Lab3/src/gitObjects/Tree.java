package gitObjects;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance("SHA-1");

            String data= getContent();
//            System.out.println(String.format("%s %s\n%s",getType(),data.getBytes().length,data.getBytes()));
            byte[] hash = messageDigest.digest(data.getBytes());

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


