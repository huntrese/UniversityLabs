package impl;

import interfaces.Stack;

import java.util.EmptyStackException;

public class AnyTypeStack<T> implements Stack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;
    private final int maxSize = 5; // Maximum size of the stack

    public AnyTypeStack() {
        head = null;
        size = 0;
    }
    @Override
    public void push(T item) {
        if (isFull()) {
            throw new IndexOutOfBoundsException("Stack is Full. Cannot add elements.");
        }
        Node<T> newNode = new Node<>(item);
        newNode.next = head;
        head = newNode;
        size++;
    }
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is Empty. Cannot remove elements.");
        }
        T item = head.data;
        head = head.next;
        size--;
        return item;
    }
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is Empty. Cannot remove elements.");
        }
        return head.data;
    }
    public void print() {
        AnyTypeStack.Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
    @Override
    public boolean isEmpty() {
        return head == null;
    }
    @Override

    public boolean isFull() {
        return size == maxSize;
    }
    @Override

    public int size() {
        return size;
    }

}
