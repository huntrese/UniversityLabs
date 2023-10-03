package src.ser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Reader {
    private final HashMap<String, String> objPaths;

    public Reader() {
        objPaths = new HashMap<>();
        objPaths.put("java/src/docs/Faculty.ser", "src.classes.Faculty");
        objPaths.put("java/src/docs/Student.ser", "src.classes.Student");
        objPaths.put("java/src/docs/Graduate.ser", "src.classes.Graduate");
        objPaths.put("java/src/docs/StudyField.ser", "src.classes.StudyField");
    }

    public <T> List<T> readObjects(String file, Class<T> clazz) {

        List<T> objectList = new ArrayList<>();

        String objClass = objPaths.get(file);
        if (objClass == null) {
            // Handle the case where the object class is not found in the mapping.
            return objectList;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            while (true) {
                try {
                    Object fileObject = Class.forName(objClass).cast(in.readObject());
                    if (clazz.isInstance(fileObject)) {
                        objectList.add(clazz.cast(fileObject));
                    }
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }

        return objectList;
    }
}
