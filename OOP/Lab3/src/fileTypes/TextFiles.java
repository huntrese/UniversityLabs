package fileTypes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFiles extends FileType{
    private int lineCount;
    private int wordCount;
    private int characterCount;


    public TextFiles(String[] fileInfo){
        super(fileInfo);
        setFileCounts();
        getInfo();
    }

    public void setFileCounts() {
        try {
            File file = new File(getName());
            Path filePath = Paths.get(file.getPath());
            byte[] content = Files.readAllBytes(filePath);
            characterCount = new String(content, StandardCharsets.UTF_8).length();
            lineCount = Files.readAllLines(filePath).size();
            wordCount = new String(content, StandardCharsets.UTF_8).split("\\s+").length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getInfo() {
        String modifiedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getModifiedTime()));
        String createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getCreatedTime()));
        String INFO = String.format("""
                File type: %s
                Created: %s
                Modified: %s
                Character count: %s
                Line count: %s
                Word count: %s
                """,getExtension(),modifiedTime,createdTime,characterCount,lineCount,wordCount);
        System.out.println(INFO);
    }

}
