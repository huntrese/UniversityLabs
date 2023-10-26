import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ref extends GitObject {
    private String content;
    public Ref(String path,String content) {
        super(".gip/refs/HEAD/"+ path);
        this.content=content;
    }

    @Override
    public String getType() {
        return "reference";
    }

    @Override
    public String createHashDir(String treePath) {

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

    @Override
    public byte[] readFileBytes() {
        try {
            return Files.readAllLines(Paths.get(getPath())).get(0).getBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String calculateSHA1() {
        return null;
    }
}