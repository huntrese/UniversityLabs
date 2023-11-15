public interface Queue<T> {
    void enqueue(T item); // Add an item to the end of the queue.
    T dequeue();         // Remove and return the item at the front of the queue.
    T peek();            // Return the item at the front of the queue without removing it.
    boolean isEmpty();   // Check if the queue is empty.
    int size();          // Return the number of elements in the queue.
}
