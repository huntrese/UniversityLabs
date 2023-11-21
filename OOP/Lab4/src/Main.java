import impl.AnyTypeQueue;
import impl.AnyTypeStack;
import impl.ArrayQueue;
import impl.StackQueue;
import interfaces.Queue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AnyTypeQueue stackQueue = new AnyTypeQueue<String>();
//        stackQueue.push("wow");
//        stackQueue.push("amazing");
        stackQueue.enqueue("I am");
        stackQueue.dequeue();
//        stackQueue.dequeue();
        stackQueue.enqueue("are");
        stackQueue.enqueue("No you");
//        stackQueue.push("!");
//        stackQueue.push("!");
        System.out.println(stackQueue.size());
        stackQueue.print();


    }
}
