public abstract class GitObject {
    private String path;
    private String name;


    public GitObject(String name, String path) {
        this.name = name;
        this.path = path;
    }

    // Abstract method to be overridden by subclasses
    public abstract String getType();

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


    public abstract String createHashDir(String objPath);

    public abstract byte[] readFileBytes();

    public abstract String calculateSHA1();
}
