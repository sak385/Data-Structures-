package queue;

/*
 * CS112 Queue Lab
 * 
 * Implement enqueue(), dequeue() using an array-based queue of strings
 * to represent a line of people waiting for a theme park attraction
 * 
 * This Queue uses an array with front and rear indices to manage the queue.
 * Elements are enqueued at the rear and dequeued from the front.
 * Uses a circular array implementation to efficiently use space.
 * 
 * @author Colin Sullivan
 */
public class ThemeParkQueue {
    
    private String[] queue;
    private int front;
    private int rear;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /*
     * Constructor which initializes the array-based queue as empty
     */
    public ThemeParkQueue() {
        queue = new String[DEFAULT_CAPACITY];
        front = 0;
        rear = -1;
        size = 0;
    }

    /**
     * Enqueue method which adds a string at the end of the line
     * and increments the size attribute.
     * 
     * If the array is full, it will be resized to accommodate more elements.
     * Uses circular array implementation where rear wraps around.
     * 
     * @param toAdd String value to enqueue
     */
    public void enqueue(String toAdd) { 
        // WRITE YOUR CODE HERE
        if(size == queue.length){
            resize();
        }
        rear = (rear + 1) % queue.length;
        queue[rear] = toAdd;
        size++;
    }

    /**
     * Dequeue method which removes the string at the front of the line and
     * decrements the size attribute
     * 
     * Returns null if the queue is empty.
     * Uses circular array implementation where front wraps around.
     * 
     * @return String which was at the front of the queue, or null if empty
     */
    public String dequeue() {
        // WRITE YOUR CODE HERE
        if(size == 0){
            return null;
        }
        String temp = queue[front];
        queue[front] = null;
        front = (front + 1) % queue.length;
        size--;
        return temp;
    }

    /*
     * Helper method to resize the array when it becomes full
     */
    private void resize() {
        // PROVIDED FOR YOU
        String[] newQueue = new String[queue.length * 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[(front + i) % queue.length];
        }
        queue = newQueue;
        front = 0;
        rear = size - 1;
    }

    /*
     * Getter method for length of line
     * 
     * @return number of riders in line (size)
     */
    public int getLineLength() {
        return size;
    }

    /*
     * Getter method for the front index of the line
     * 
     * @return index at the front of the queue, or null if empty
     */
    public int getFront() {
        return this.front;
    }

    /**
     * Returns the queue
     * @return String[] queue
     */
    public String[] getQueue() {
        return queue;
    }

}
