public class LinkedQueue<T> implements Queue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
        }
        T item = front.data;
        front = front.next;
        size--;
        if (isEmpty()) {
            rear = null; // Reset rear when the queue becomes empty.
        }
        return item;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
        }
        return front.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }
}
