package gitObjects;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ref {
    private String path;
    private String name;
    private final String refPath = ".gip/refs/";
    private final String objPath = ".gip/objects/";

    public Ref(String name) {
        this.name = name;
        this.path = refPath + name;
    }

    public void changeHead(String name) {
        try {
            this.name = name;
            Path branchpath = Paths.get(refPath+"/"+name);
            Path commitpath = Paths.get(objPath+"/"+name.substring(0,2)+"/"+name.substring(2));
            System.out.println(branchpath);
            System.out.println(commitpath);

            if (Files.exists(branchpath)){
                System.out.println("found");
            } else if(Files.exists(commitpath)){
                System.out.println("commit");
            }
            Files.write(Paths.get(getPath()), ("/"+name).getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void initialize() {
        try {
            Files.write(Paths.get(getPath()), "master".getBytes());
            Files.write(Paths.get(refPath+"/master"), "".getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Branch getBranch(){
        File head = new File(getPath());
        String branchName = new String(gitHelper.readFileBytes(head.getPath()), StandardCharsets.UTF_8);
        if(!Files.exists(Paths.get(refPath+"/"+branchName))){
            return null;
        }
        return new Branch(branchName);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}