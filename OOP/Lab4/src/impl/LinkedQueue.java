    package impl;

    import interfaces.Queue;

    import java.util.EmptyStackException;

    public class LinkedQueue<T> implements Queue<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size;
        private final int maxSize = 5; // Maximum size of the queue

        private static class Node<T> {
            T data;
            Node<T> next;

            Node(T data) {
                this.data = data;
                this.next = null;
            }
        }

        public LinkedQueue() {
            head = null;
            tail = null;
            size = 0;
        }

        @Override
        public void enqueue(T item) {
            if (isFull()) {
                throw new IndexOutOfBoundsException("Queue is full. Cannot add more elements.");

            }
            Node<T> newNode = new Node<>(item);
            if (isEmpty()) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        @Override
        public T dequeue() {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException("Queue is Empty. Cannot remove elements.");

            }
            T item = head.data;
            head = head.next;
            size--;
            if (isEmpty()) {
                tail = null; // Reset tail when the queue becomes empty.
            }
            return item;
        }

        @Override
        public T peek() {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException("Queue is Empty. Cannot remove elements.");
            }
            return head.data;
        }
        public void print() {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException("Queue is Empty. Cannot remove elements.");

            }

            Node<T> current = head;

            while (current != null) {
                System.out.print(current.data + " ");
                current = current.next;
            }

            System.out.println();
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public int size() {
            return size;
        }

        public boolean isFull() {
            return size == maxSize;
        }
    }
