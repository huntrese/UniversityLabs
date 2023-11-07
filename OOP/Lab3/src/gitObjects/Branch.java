package gitObjects;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public record Branch(String path) {
    public Branch(String path) {

        this.path = ".gip/refs/" + path;
    }
    public void createBranch(String treePath) {

        try {


            File file = new File(treePath);

            Path filePath = getObj(treePath);
            BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
            BasicFileAttributes basicAttributes = attributes.readAttributes();
            Files.write(Paths.get(getPath()), (file.getName() + "\n" + basicAttributes.creationTime()).getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] readReference() {
        try {
            return Files.readAllLines(Paths.get(getPath())).get(0).getBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readSnapTime() {
        try {
            return Files.readAllLines(Paths.get(getPath())).get(1).getBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getObj(String findObj) {
        String objPath = ".gip/objects";
        File objects = new File(objPath);
        for (File file : objects.listFiles()) {
            if (findObj.substring(0, 2).equals(file.getName())) {
                for (File object : file.listFiles()) {
                    if (findObj.substring(2).equals(object.getName())) {
                        return Paths.get(object.getPath());
                    }
                }
            }
        }


        return null;
    }

    public String getPath() {
        return path;
    }

}