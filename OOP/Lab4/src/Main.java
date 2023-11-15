public class Main {
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
        System.out.println(stackQueue.size());
        stackQueue.print();

    }
}
