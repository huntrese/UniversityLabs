package impl;

import interfaces.Stack;

import java.util.LinkedList;

public class ArrayStack<T> implements Stack<T> {
    private LinkedList<T> list;
    private final int maxSize = 5; // Maximum size of the stack

    public ArrayStack() {
        list = new LinkedList<>();
    }

    @Override
    public void push(T item) {
        if (isFull()) {
            throw new IndexOutOfBoundsException("Stack is Full. Cannot add elements.");
        }
        list.addFirst(item);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is Empty. Cannot remove elements.");
        }
        return list.removeFirst();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is Empty.");
        }
        return list.getFirst();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    public void print(){
        for (Object item: list  ) {
            System.out.print(item+" ");
        }
        System.out.println();
    }

    public boolean isFull() {
        return size() == maxSize;
    }
}
