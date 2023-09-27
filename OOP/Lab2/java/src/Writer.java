package src;

import java.io.*;
import java.util.List;
import java.util.HashMap;

public class Writer {
    HashMap<String, String> objPaths = new HashMap<>();

    public Writer() {
        objPaths.put("Faculty", "src/docs/Faculty.ser");
        objPaths.put("Student", "src/docs/Student.ser");
        objPaths.put("Graduate", "src/docs/Graduate.ser");
        objPaths.put("StudyField", "src/docs/StudyFiled.ser");
    }

    public <T> void writeObjects(List<T> objects) {
        if (objects.isEmpty()) {
//            System.out.println("ooops");

            return;
        }

        try {
            String file = objPaths.get(objects.get(0).getClass().getSimpleName());
//            System.out.println(file);

            FileOutputStream fileOut = new FileOutputStream(file);


            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            for (T obj : objects) {
                out.writeObject(obj);
            }


            out.close();
            fileOut.close();

            System.out.println(String.format("Objects have been saved into %s", file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
