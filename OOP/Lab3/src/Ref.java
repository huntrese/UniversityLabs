public class Ref extends GitObject {
    public Ref(String name,String content) {
        super(name,content);
    }

    @Override
    public String getType() {
        return "reference";
    }

    @Override
    public String createHashDir(String objPath) {
        return null;
    }

    @Override
    public byte[] readFileBytes() {
        return new byte[0];
    }

    @Override
    public String calculateSHA1() {
        return null;
    }
}