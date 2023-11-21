import impl.StackQueue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String[] getDataStructureImplementations() {
        List<String> classNames = new ArrayList();
        File directory = new File("out/production/Lab4/impl");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> {
                return name.endsWith(".class") && !name.contains("$");
            });
            if (files != null) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    String className = file.getName().replace(".class", "");
                    classNames.add(className);
                }
            }
        }

        return (String[])classNames.toArray(new String[0]);
    }
    public static void main(String[] args) {
        StackQueue stackQueue = new StackQueue();
        stackQueue.push("wow");
        stackQueue.push("amazing");
        stackQueue.enqueue("I am");
        stackQueue.dequeue();
        stackQueue.dequeue();
        stackQueue.enqueue("are");
        stackQueue.enqueue("No you");
        stackQueue.push("!");
        stackQueue.push("!");
        System.out.println(stackQueue.size());
        stackQueue.print();
//        for(String str: getDataStructureImplementations()) {
//            System.out.println(str);
//        }

    }
}
