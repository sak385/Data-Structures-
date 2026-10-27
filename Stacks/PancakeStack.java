package stack;

import edu.rutgers.cs112.LL.LLNode;

/**
 * The class that will be filled out and submitted
 * The push() and pop() methods need to be implemented
 * @author Sulaiman Hasan
 */
public class PancakeStack {
    private LLNode<String> topPancake; // reference to the top of the stack
    private int numPancakes; // how many pancakes are in the stack

    /**
     * Default constructor, used by the Driver
     * Creates a new stack and starts it as null
     */
    public PancakeStack(){
        // DO NOT edit or remove 
        topPancake = null;
        numPancakes = 0;
    }

    /**
     * Add (cook) a new pancake to the top of the stack
     * Create a new node with pancakeType as the data, and make this node the top of the stack
     * 
     * @param pancakeType The flavor data for the new node being pushed
     */
    public void push(String pancakeType){
        // WRITE YOUR CODE HERE
        LLNode<String> newPancake = new LLNode<String>(pancakeType);
        //setNext(LLNode<T> nextNode) – sets the next node of the current node to nextNode
        if(topPancake == null){
            topPancake = newPancake;
        }
        else{
            newPancake.setNext(topPancake);
            topPancake = newPancake;
        }
        numPancakes++;
    }

    /**
     * Remove (eat) the pancake at the top of the stack, and return the pancake type of the removed node
     * If the stack is already empty, then return null
     * 
     * @return The pancake type of the node which was removed, or null if there were no nodes
     */
    public String pop(){
        // WRITE YOUR CODE HERE
        if(topPancake != null){
            LLNode<String> oldPancake = topPancake;
            topPancake = topPancake.getNext();
            numPancakes--;
            return oldPancake.getData();
        }
        return null; //if theres no pancakes return null
    }

    /**
     * Used by the Driver
     * @returns The nodes in the stack
     */
    public LLNode<String> getStack(){
        // DO NOT edit or remove
        return topPancake;
    }

    /**
     * Used by the Driver
     * @returns the number of pancakes in the stack
     */
    public int getNumPancakes(){
        // DO NOT edit or remove
        return numPancakes;
    }

}
