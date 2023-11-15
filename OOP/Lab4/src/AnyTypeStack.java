import java.util.EmptyStackException;

public class AnyTypeStack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top;
    private int size;

    public AnyTypeStack() {
        top = null;
        size = 0;
    }

    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("Stack is Empty.");
            return null;
        }
        T item = top.data;
        top = top.next;
        size--;
        return item;
    }

    public T peek() {
        if (isEmpty()) {
            System.out.println("Stack is Empty.");
            return null;        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        AnyTypeStack<Object> stack = new AnyTypeStack<>();

        stack.push(1);
        stack.push("Hello, world!");
        stack.push(new CustomObject("Custom Data"));

        System.out.println("Top element: " + stack.peek());
        System.out.println("Stack size: " + stack.size());

        while (!stack.isEmpty()) {
            System.out.println("Popped: " + stack.pop());
        }
    }

    private static class CustomObject {
        private String data;

        CustomObject(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data;
        }
    }
}
