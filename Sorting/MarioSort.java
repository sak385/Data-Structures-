package mario;

/**
 * This class implements two sorting algorithms, Insertion Sort and Merge Sort.
 * The Insertion Sort is used to sort an array of Racer objects, based on their average lap times.
 * The Merge Sort is used to sort an array of Player objects, based on their stars and coins.
 * 
 * @author Maaz Mansuri
 * @author George Janulis 
 */
public class MarioSort { 

    private Player[] marioParty; 
    private Racer[] marioKart;  
   
    /**
     * This methods implements Insertion Sort, in order to sort
     * the average lap times of marioKart racers.
     * 
     * View the assignment description for detail on how to implement insertion sort.
     */
    public void marioKartSort(){
        // WRITE YOUR CODE HERE
        //racer.getavgLapTime()
        if(marioKart == null || marioKart.length == 0) {
            return;
        }
        for (int i = 1; i < marioKart.length; i++) {
            Racer key = marioKart[i];
            double keyAvg = key.getAverage();
            int j = i-1;
            while (j >= 0 && marioKart[j].getAverage() > keyAvg) {
                marioKart[j + 1] = marioKart[j];
                j--;
            }
            marioKart[j + 1] = key;
        }
   
    } 

    /**
     * This is a helper method for merge sort, which handles the 
     * the recursive calls.
     * 
     * View the assignment description for details on how to implement.
     * 
     * @param arr The array to recursively merge sort
     * @param left The left bound of the array
     * @param right The right bound of the array
     */
    public void mergeHelper(Player arr[], int left, int right){
        // WRITE YOUR CODE HERE
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeHelper(arr, left, middle);
            mergeHelper(arr, middle + 1, right);
            merge(arr, left, middle, right);
        }
    }
     
    /**
     * This method is provided to you. 
     * 
     * This calls merge sort, for a group of mario party players.
     * It uses the mergeHelper(), which you code, to run merge sort.
     * 
     * It will sort the this.marioParty array by stars then coins.
     */
    public void marioPartySort(){ 
        // DO NOT EDIT
        if (marioParty != null && marioParty.length > 0) {
            mergeHelper(marioParty, 0, marioParty.length - 1);
        }
    } 
   
    /**
     * This method is provided for you.
     * 
     * This method merges halves of an array, by sorting them together.
     * This is a crucial element of merge sort, which handles the actual 
     * sorting found in the algorithm.
     * 
     * @param arr The array to merge/sort
     * @param left The left bound of the array
     * @param middle The middle index of the array
     * @param right The right bound of the array
     */
    public void merge(Player arr[], int left, int middle, int right){
        // DO NOT EDIT
        int size1 = middle - left + 1;
        int size2 = right - middle;

        Player newL [] = new Player[size1];
        Player newR [] = new Player[size2];

        for (int i = 0; i < size1; ++i){
            newL[i] = arr[left + i];
        }

        for (int j = 0; j < size2; ++j){
            newR[j] = arr[middle + 1 + j];
        }

        int i = 0, j = 0;
        int k = left;

        while (i < size1 && j < size2){ 
            if (newL[i].getStars() > newR[j].getStars()){
                arr[k] = newL[i];
                i++;
            }
            else if (newL[i].getStars() < newR[j].getStars()){
                arr[k] = newR[j];
                j++;
            }
            else {  
                if (newL[i].getCoins() >= newR[j].getCoins()){
                    arr[k] = newL[i];
                    i++;
                }
                else {
                    arr[k] = newR[j];
                    j++;
                }
            }
            k++;
        }

        while (i < size1){
            arr[k] = newL[i];
            i++;
            k++;
        } 
        while (j < size2){
            arr[k] = newR[j];
            j++;
            k++;
        }
    }

    /**
     * This method is provided to you
     * 
     * Reads in data from the corresponding input file, into the correct array
     * depending on the filename 
     * 
     * @param filename
     */
    public void readData(String filename) { 
        // DO NOT EDIT
        StdIn.setFile(filename);
        String[] lines = StdIn.readAllLines();
        if (filename.toLowerCase().contains("kart")) { 
            marioKart = new Racer[lines.length-1]; 
        } else if (filename.toLowerCase().contains("party")) {
            marioParty = new Player[lines.length-1];
        }
        for (int i = 1; i < lines.length; i++) { // skip header
            String[] split = lines[i].split(",");
            if (filename.toLowerCase().contains("kart")) {
                int[] times = new int[]{
                    Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])}; 
                marioKart[i-1] = new Racer(split[0], times);
            } else if (filename.toLowerCase().contains("party")) {
                marioParty[i-1] = new Player(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
        }
    }  
 
    public Racer[] getMarioKart() {
        return marioKart;
    }

    public Player[] getMarioParty() {
        return marioParty;
    }
 
}
