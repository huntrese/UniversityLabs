import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ref {
    private String path;
    public Ref(String path) {
        this.path=".gip/refs/HEAD/"+ path;
    }


    public String getPath() {
        return path;
    }

    public String createBranch(String treePath) {

        try {



            System.out.println("SHA-1 hash: " + treePath + "   "+getPath());
            File file = new File(treePath);
            file.getName();

            Files.write(Paths.get(getPath()),file.getName().getBytes());


        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Master updated";
    }


    public byte[] readReference() {
        try {
            return Files.readAllLines(Paths.get(getPath())).get(0).getBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}