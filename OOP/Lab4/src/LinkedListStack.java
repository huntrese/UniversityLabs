import java.util.EmptyStackException;
import java.util.LinkedList;

public class LinkedListStack<T> implements Stack<T> {
    private LinkedList<T> list;

    public LinkedListStack() {
        list = new LinkedList<>();
    }

    @Override
    public void push(T item) {
        list.addFirst(item);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            System.out.println("Stack is Empty.");
            return null;
        }
        return list.removeFirst();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            System.out.println("Stack is Empty.");
            return null;
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
}
