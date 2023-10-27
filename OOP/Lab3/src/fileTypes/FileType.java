package fileTypes;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public abstract class FileType {
    private final String name;
    private final String extension;
    private Long modifiedTime;
    private Long createdTime;

    public FileType(String[] fileInfo){

        this.name = fileInfo[0]+"."+fileInfo[1];
        this.extension = fileInfo[1];
        setFileTimes();
    }
    private void setFileTimes(){

        try {
            BasicFileAttributeView attributes = java.nio.file.Files.getFileAttributeView(Paths.get(name), BasicFileAttributeView.class);
            BasicFileAttributes basicAttributes = attributes.readAttributes();
            modifiedTime = basicAttributes.lastModifiedTime().toMillis();
            createdTime = basicAttributes.creationTime().toMillis();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public Long getModifiedTime() {
        return modifiedTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }


    public abstract void getInfo();


}
