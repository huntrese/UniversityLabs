package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Reader {
    HashMap<String, String> objPaths = new HashMap<>();

    public Reader() {
        objPaths.put("src/docs/Faculty.ser", "src.Faculty");
        objPaths.put("src/docs/Student.ser", "src.Student");
        objPaths.put("src/docs/StudyFiled.ser", "src.StudyField");
    }

    public <T> List<T> readObjects(String file, Class<T> clazz) {
        List<T> objectList = new ArrayList<>();

        try {
            String objClass = objPaths.get(file);

            FileInputStream fileIn = new FileInputStream(file);


            ObjectInputStream in = new ObjectInputStream(fileIn);


            while (true) {
                try {
                    Object fileObject = (Class.forName(objClass)).cast(in.readObject());
                    if (clazz.isInstance(fileObject)) {
                        objectList.add(clazz.cast(fileObject));
                    }
                } catch (EOFException e) {

                    break;
                }
            }


            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objectList;
    }
}
