package gitObjects;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class gitHelper {
    public static byte[] readFileBytes(String path) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream inputStream = new FileInputStream(System.getProperty("user.dir")+ "/"+path)) {
            int bytesRead;
            byte[] data = new byte[1024];
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return buffer.toByteArray();

    }
    public static Path createHashDirHelper(String objPath, String sha1Hash){
        try {
            String fileHashDir = objPath + "/" + sha1Hash.substring(0, 2);
            Path fileHashDirPath = Paths.get(fileHashDir);
            Path fileHashPath = Paths.get(fileHashDir + "/" + sha1Hash.substring(2));
            if (!Files.exists(fileHashDirPath)) {
                Files.createDirectory(fileHashDirPath);
            } else {
                Files.delete(fileHashPath);
            }
            Files.createFile(fileHashPath);

            return fileHashPath;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    static StringBuilder getString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hash) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString;
    }
}
