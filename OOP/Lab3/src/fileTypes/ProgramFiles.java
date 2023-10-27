package fileTypes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgramFiles extends FileType{
    private int lineCount;
    private int classCount;
    private int methodCount;

    public ProgramFiles(String[] fileInfo){
        super(fileInfo);
        setFileCounts();
        getInfo();
    }
    public void setFileCounts() {
        try {
            String classPattern="";
            String methodPattern="";
            switch (getExtension()) {
                case "java" -> {
                    classPattern = "class\\s+\\w+";
                    methodPattern = "(public|protected|private|static|\\s) +[\\w<>\\[\\]]+\\s+(\\w+)\\s*\\([^)]*\\)\\s*(\\{?|[^;])";
                }
                case "py" -> {
                    classPattern = "class\\s+\\w+";
                    methodPattern = "def\\s+(\\w+)\\s*\\([^)]*\\):";
                }
            }
            File file = new File(getName());
                Path filePath = Paths.get(file.getPath());
                byte[] content = Files.readAllBytes(filePath);
                lineCount = Files.readAllLines(filePath).size();

                classCount = new String(content, StandardCharsets.UTF_8).split(classPattern).length - 1;

                methodCount = new String(content, StandardCharsets.UTF_8).split(methodPattern).length - 1;

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
                Line count: %s
                Class count: %s
                Method count: %s
                """,getExtension(),modifiedTime,createdTime,lineCount,classCount,methodCount);
        System.out.println(INFO);
    }
}
