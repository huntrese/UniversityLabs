package interfaces;

public interface Stack<T> {
    void push(T item);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
    boolean isFull(); // New method to check if the stack is full
}
