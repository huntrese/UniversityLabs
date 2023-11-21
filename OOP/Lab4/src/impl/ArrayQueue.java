package impl;

import interfaces.Queue;

import java.util.EmptyStackException;

public class ArrayQueue<T> implements Queue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;
    private final int maxSize=5;

    private static class Node<T> {
        T data;
        Node<T> next;
    }

    public ArrayQueue() {
        rear = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }

        Node<T> newNode = new Node<>();
        newNode.data = item;

        if (isEmpty()) {
            front = newNode;
            rear = front;
            newNode.next = newNode;
        } else {
            // Insert the new node at the front of the queue
            newNode.next = front;
            front = newNode;
            // Update the rear pointer if the queue was empty before
            if (isEmpty()) {
                rear = front;
            }
        }

        size++;
    }



    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        T item = front.data;

        if (size == 1) {
            front = null;
            rear = null;
        } else {
            front = front.next;
            rear.next = front;
        }

        size--;
        return item;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
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

    public boolean isFull() {
        return size == maxSize;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        Node<T> current = front;
        int count = 0;

        do {
            System.out.print(current.data + " ");
            current = current.next;
            count++;

            // Check if the loop has iterated through the entire queue
            if (count >= size) {
                break;
            }
        } while (current != front);
        System.out.println();
    }


}
