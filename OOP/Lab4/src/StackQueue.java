public class StackQueue<T> implements Queue<T>, Stack<T> {
    private Node<T> head; // Head node of the doubly linked list
    private Node<T> tail; // Tail node of the doubly linked list
    private int size;

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    public StackQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return null;
        }

        T item = head.data;
        Node<T> next = head.next;

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = next;
            head.prev = null;
        }

        size--;
        return item;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return null;
        }
        return tail.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    // Stack operations

    @Override
    public void push(T item) {
        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            System.out.println("StackQueue is empty");
            return null;
        }

        T item = head.data;
        Node<T> nextHead = head.next;

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = nextHead;
            head.prev = null;
        }

        size--;
        return item;
    }

    public void print() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}
