package tree;
import edu.rutgers.cs112.Comparable112;

/**
 * The Flower class represents a flower with a heat signature and the type of bug it holds.
 * It implements the Comparable112 interface to allow comparison based on heat signatures.
 * 
 * @author Ayla Muminovic
 * @author Christian Parra
 */
public class Flower extends Comparable112<Flower>{
    
    private int heatSignature;
    private Bug holds; 

    public Flower(int heatSignature, Bug holds)
    {
        this.heatSignature = heatSignature;
        this.holds = holds;
    }

    public int getHeatSignature() {
        return heatSignature;
    }

    public Bug getHolds() {
        return holds;
    }

    @Override
    public int compareTo(Flower arg0) {
        if (this.heatSignature < arg0.heatSignature) {
            return -1;
        } else if (this.heatSignature > arg0.heatSignature) {
            return 1;
        } else {
            return 0; // heat signatures are equal
        }
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Flower) {
            Flower other = (Flower) arg0;
            return this.heatSignature == other.heatSignature && this.holds == other.holds;
        }
        return false;
    }
 
    @Override
    public String toString() {  
        return heatSignature + "°C " + getIcon() + " (" + holds.getName() + " " + getIcon() +")";
    }

    public String getIcon() {
        if(holds == Bug.BUTTERFLY){
            return "🦋";
        } else if (holds == Bug.LADYBUG){
            return "🐞"; 
        }
        else if(holds == Bug.LANTERNFLY){
            return  "🕷️"; 
        }
        else if (holds == Bug.EMPTY){
            return "🌺";
        } 
        else {
            return "🌳";  
        }
    }
    

}
