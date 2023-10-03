package src.ser;

import java.io.*;
import java.util.List;
import java.util.HashMap;

public class Writer {
    HashMap<String, String> objPaths = new HashMap<>();

    public Writer() {
        objPaths.put("Faculty", "java/src/docs/Faculty.ser");
        objPaths.put("Student", "java/src/docs/Student.ser");
        objPaths.put("Graduate", "java/src/docs/Graduate.ser");
        objPaths.put("StudyField", "java/src/docs/StudyFiled.ser");
    }

    public <T> void writeObjects(List<T> objects) {
        if (objects.isEmpty()) {

            return;
        }

        try {
            String file = objPaths.get(objects.get(0).getClass().getSimpleName());
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            for (T obj : objects) {
                out.writeObject(obj);
            }


            out.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
