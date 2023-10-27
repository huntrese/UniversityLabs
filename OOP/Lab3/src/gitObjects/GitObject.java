package gitObjects;

public abstract class GitObject {
    private final String path;


    public GitObject(String path) {
        this.path = path;
    }

    // Abstract method to be overridden by subclasses
    public abstract String getType();

    public String getPath() {
        return path;
    }


    public abstract String createHashDir(String objPath);

    public abstract byte[] readFileBytes();

    public abstract String calculateSHA1();
}
