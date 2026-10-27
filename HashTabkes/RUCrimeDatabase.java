package crime;
import edu.rutgers.cs112.LL.LLNode;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
/**
 * Students will be analyzing cybercrime incident data using hash tables (with separate chaining) 
 * and linked lists to organize and query crime records efficiently. The theme revolves around 
 * parsing, storing, and analyzing real-world crime logs from Rutgers University’s public 
 * safety database: Daily Crime & Fire Safety Log.
 * 
 * @author Anna Lu
 * @author Krish Lenka
 */
public class RUCrimeDatabase {
    private LLNode<Incident>[] incidentTable; // Array of LLNodes
    private int totalIncidents;
    private static final double LOAD_FACTOR_THRESHOLD = 4.0;

    /**
     * Default constructor initializes the hash table with a size of 10.
     * The total number of incidents is set to zero.
     */
    public RUCrimeDatabase() {
        incidentTable = new LLNode[10];
        totalIncidents = 0;
    }

    /**
     * Adds a new incident to the hash table
     * @param incident An incident object which we will use to add to the hash table:
     */
    public void addIncident(Incident incident) {
        // WRITE YOUR CODE HERE
        if (incident == null) {
            return;
        }

        String key = incident.getIncidentNumber();
        int index = hashFunction(key);

        // Insert at front
        LLNode<Incident> newNode = new LLNode<>(incident);
        newNode.setNext(incidentTable[index]);
        incidentTable[index] = newNode;
        totalIncidents++;

        // Check load factor & rehash if needed
        double loadFactor = (double) totalIncidents / incidentTable.length;
        if (loadFactor >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }

    }

    /**
     * Reads the csv file, creates an Incident object for each line, and calls addIncident().
     * @param filename Path to file containing incident data
     */
    public void buildIncidentTable(String inputfile) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(inputfile);

        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine();
            if (line == null || line.trim().isEmpty()){
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 7) continue;

            String incNum = parts[0];
            String nature = parts[1];
            String report = parts[2];
            String occurrence = parts[3];
            String location = parts[4];
            String disposition = parts[5];
            String genLocation = parts[6];
            Category category = Category.fromString(nature);
            Incident inc = new Incident(incNum, nature, report, occurrence, location, disposition, genLocation, category);

            addIncident(inc);
        }
        
    }
 

    /**
     * Rehashes the incident groups in the hash table.
     * This is called when the load factor exceeds a certain threshold.
     */
    public void rehash() {
        // WRITE YOUR CODE HERE
        LLNode<Incident>[] oldTable = incidentTable;
        incidentTable = new LLNode[oldTable.length * 2];
        totalIncidents = 0;

        for (LLNode<Incident> head : oldTable) {
            LLNode<Incident> curr = head;
            while (curr != null) {
                addIncident(curr.getData());
                curr = curr.getNext();
            }
        }
        
    } 

    /**
     * Deletes an incident based on its incident number.
     * @param incidentNumber The incident number of the incident to delete 
     */
    public void deleteIncident(String incidentNumber) {
        // WRITE YOUR CODE HERE
        int index = hashFunction(incidentNumber);
        LLNode<Incident> curr = incidentTable[index];
        LLNode<Incident> prev = null;

        while (curr != null) {
            if (curr.getData().getIncidentNumber().equals(incidentNumber)) {
                if (prev == null) {
                    incidentTable[index] = curr.getNext();
                } else {
                    prev.setNext(curr.getNext());
                }
                totalIncidents--;
                return;
            }
            prev = curr;
            curr = curr.getNext();
        }
        
    }

    /**
     * Iterates over another RUCrimeDatabase's incident table, and adds its incidents
     * to this table IF they do not already exist.
     * @param other RUCrimeDatabase to copy new incidents from
     */
    public void join(RUCrimeDatabase other) {
        // WRITE YOUR CODE HERE
        LLNode<Incident>[] otherTable = other.getIncidentTable();

        for (LLNode<Incident> head : otherTable) {
            LLNode<Incident> curr = head;
            while (curr != null) {
                Incident inc = curr.getData();
                String incNum = inc.getIncidentNumber();

                // Check if it already exists
                int index = hashFunction(incNum);
                boolean found = false;
                LLNode<Incident> search = incidentTable[index];

                while (search != null) {
                    if (search.getData().getIncidentNumber().equals(incNum)) {
                        found = true;
                        break;
                    }
                    search = search.getNext();
                }
                if (!found) {
                    addIncident(inc);
                }
                curr = curr.getNext();
            }
        }
        
    } 
    
    /**
     * Returns a list of the top K locations with the most incidents 
     * If K > numLocations, return all locations
     * @return ArrayList<String> containing the top K locations
     */
    public ArrayList<String> topKLocations(int K) {
        // WRITE YOUR CODE HERE
        String[] locations = {"ACADEMIC", "CAMPUS SERVICES", "OTHER", "PARKING LOT", "RECREATION", "RESIDENTIAL", "STREET/ROADWAY"};
        int[] counts = new int[locations.length];

        // Count occurrences
        for (LLNode<Incident> head : incidentTable) {
            LLNode<Incident> curr = head;
            while (curr != null) {
                String g = curr.getData().getGeneralLocation();
                for (int i = 0; i < locations.length; i++) {
                    if (g.equals(locations[i])) {
                        counts[i]++;
                    }
                }
                curr = curr.getNext();
            }
        }

        // Build list of pairs
        ArrayList<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < locations.length; i++) {
            pairs.add(new int[]{i, counts[i]});
        }

        // Sort descending by count
        Collections.sort(pairs, (a, b) -> Integer.compare(b[1], a[1]));
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < pairs.size() && i < K; i++) {
            result.add(locations[pairs.get(i)[0]]);
        }
        
        return result;
    }  

    /**
     * Returns the percentage of incidents for every category.
     * Categories: Property, Violent,
     *             Mischief, Trespass, or Other
     * @return A HashMap<Category, Double> with percentage of incidents of each category
     */
    public HashMap<Category, Double> natureBreakdown() { 
        // WRITE YOUR CODE HERE
        HashMap<Category, Double> map = new HashMap<>();

        // Initialize categories with zero
        map.put(Category.PROPERTY, 0.0);
        map.put(Category.VIOLENT, 0.0);
        map.put(Category.MISCHIEF, 0.0);
        map.put(Category.TRESPASS, 0.0);
        map.put(Category.OTHER, 0.0);

        // Count
        for (LLNode<Incident> head : incidentTable) {
            LLNode<Incident> curr = head;
            while (curr != null) {
                Category c = curr.getData().getCategory();
                map.put(c, map.get(c) + 1);
                curr = curr.getNext();
            }
        }

        // Convert to percentages
        for (Category c : map.keySet()) {
            double pct = 100 * (map.get(c) / totalIncidents);
            map.put(c, pct);
        }

        return map;
    }

    //Given methods
    /**
     * DO NOT MODIFY THIS METHOD.
     * Returns the hash table array for inspection/testing
     * @return The array of LLNode<IncidentGroup> representing the hash table
     */
    public LLNode<Incident>[] getIncidentTable() {
        return incidentTable;
    }

    public void setIncidentTable(LLNode<Incident>[] incidentTable) {
        this.incidentTable = incidentTable;
    }

    public int numberOfIncidents() {
        return totalIncidents;
    }

    /**
     * DO NOT MODIFY THIS METHOD.
     * Returns the index in the hash table for a given incident number.
     * @return The index in the hash table for the incident number
     * @param incidentNumber The incident number to hash
     */
    private int hashFunction(String incidentNumber) {
        String last5Digits = incidentNumber.substring(Math.max(0, incidentNumber.length() - 5));
        int val = Integer.parseInt(last5Digits) % incidentTable.length;
        //System.out.println("Hashing incident number: " + last5Digits + " val: " + val);
        return val;
    }

}