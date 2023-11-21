package impl;

import interfaces.Queue;

public class AnyTypeQueue<T> implements Queue<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public AnyTypeQueue() {
        head = null;
        tail = null;
        size = 0;
    }

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
            throw new IndexOutOfBoundsException("Queue is empty. Cannot peek elements.");
        }
        return head.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isFull() {
        return size==5;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        AnyTypeStack<T> stack = new AnyTypeStack<>();
        Node<T> current = head;

        while (current != null) {
            stack.push(current.data);
            current = current.next;
        }

        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }

        System.out.println();
    }
}
