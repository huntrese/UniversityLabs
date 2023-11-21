package impl;

import interfaces.Queue;
import java.util.ArrayList;

public class ArrayQueue<T> implements Queue<T> {
    private ArrayList<T> data;

    public ArrayQueue() {
        data = new ArrayList<>();
    }

    @Override
    public void enqueue(T item) {
        if (isFull()) {
            throw new IndexOutOfBoundsException("Queue is Full. Cannot add elements.");
        }
        data.add(0,item);

    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Queue is Empty. Cannot remove elements.");
        }
        return data.remove(0);
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Queue is full.");
        }
        return data.get(0);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isFull() {
        return data.size()==5;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        for (T item : data) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
}
