package tree;
import edu.rutgers.cs112.BST.BSTNode;

/**
* The Lanternflies class implements a binary search tree (BST) to manage Flower nodes.
* It provides methods to add flowers based on their heat signatures and remove lanternflies
* using Hibbard deletion.
*
* @author Ayla Muminovic
* @author Christian Parra
*/    
public class Lanternflies {  

    private BSTNode<Flower> root; 

    
    /**
     * Here you will create a flower node from a given heat signature.
     * A flower node has the int heatSignature and Bug type.
     * 
     * Then, you will insert the flower node into the BST based on the heat signature value.
     * 
     * @param heatSignature
     */
    public void add(int heatSignature) {
        // WRITE YOUR CODE HERE
        BSTNode<Flower> newFlower = new BSTNode<Flower>(new Flower(heatSignature, Bug.fromTemp(heatSignature)));
        if(root == null){
            root = newFlower;
        } 
        else {
            BSTNode<Flower> current = root;
            while(true){
                if(heatSignature < current.getData().getHeatSignature()){
                    if(current.getLeft() == null){
                        current.setLeft(newFlower);
                        break;
                    } 
                    else {
                        current = current.getLeft();
                    }
                } 
                if(heatSignature > current.getData().getHeatSignature()) {
                    if(current.getRight() == null){
                        current.setRight(newFlower);
                        break;
                    } 
                    else {
                        current = current.getRight();
                    }
                }
                if(heatSignature == current.getData().getHeatSignature()){
                    // Do not add duplicates
                    break;
                }
            }
        }
    }

    /**
    * This method is provided to you. 
    * This calls your remove() method to remove 
    * the given heat signature, using Hibbard deletion for BSTs.
    * @param heatSignature the heat signature value to be deleted.
    */
    public void remove(int heatSignature){
        //DO NOT EDIT
        root = remove(root, heatSignature);
    }

   
    /**
     * Here you will delete the flower node that holds a specific heat signature.
     * 
     * @param BSTNode<Flower> the current flower node being evaluated.
     * @param heatSignature the heat signature value to be deleted.
     * @return the updated root node.
     */
    public BSTNode<Flower> remove(BSTNode<Flower> curr, int heatSignature){
        //WRITE YOUR CODE HERE 
        if(curr == null){
            return null;
        }
        else if(heatSignature < curr.getData().getHeatSignature()){
            curr.setLeft(remove(curr.getLeft(), heatSignature));
        }
        else if(heatSignature > curr.getData().getHeatSignature()){
            curr.setRight(remove(curr.getRight(), heatSignature));
        }
        else{
            // Node with only one child or no child
            if(curr.getLeft() == null){
                return curr.getRight();
            }
            if(curr.getRight() == null){
                return curr.getLeft();
            }
            BSTNode<Flower> temp = findMin(curr.getRight());
            curr.setRight(remove(curr.getRight(), temp.getData().getHeatSignature()));
            temp.setLeft(curr.getLeft());
            temp.setRight(curr.getRight());
            return temp;
        }
        return curr;
    }



    /**
    * This method is provided for you.
    * Helper method to find the minimum node of a tree 
    
    * @param BSTNode<Flower> the current flower node being evaluated.
    * @return minimum nude of a tree.
    */
    private BSTNode<Flower> findMin(BSTNode<Flower> curr){
        // DO NOT EDIT
        while (curr != null && curr.getLeft() != null) {
            curr = curr.getLeft();
        }
        return curr;
    }

    /**
    * This method is provided for you, do not edit it.
    * 
    * @return the root node of this BST.
    */
    public BSTNode<Flower> getRoot(){
        //DO NOT EDIT 
        return root;
    }

}