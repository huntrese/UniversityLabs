package src.logging;

public class UniqueIdentifierGenerator {

    public static String generateUniqueIdentifier() {
        String uniqueID = System.getProperty("user.name") + "/" + System.getProperty("os.name");
        return (uniqueID+Integer.toString(uniqueID.hashCode()));
    }


}
