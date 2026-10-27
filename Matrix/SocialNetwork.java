package graph;
import java.util.ArrayList;

/**
 * CS112 Graph Lab
 * 
 * Implement the addProfile(), searchFriend(), allFriends() addFriend() using an adjacency matrix
 * based representation of an undirected graph
 * 
 * @author Juliania Shyprykevych 
 * @author Pooja Kedia
 * @author Matthew Specht
 */
public class SocialNetwork{

    public ArrayList<Profile> profiles;
    public boolean[][] graph;

    public SocialNetwork(){
        this.profiles = new ArrayList<>();
        this.graph = new boolean[0][0];
    } 

    public void addProfile(Profile newProfile) {
        // WRITE YOUR CODE HERE
        // If already exists, do nothing
        if (profiles.contains(newProfile)) {
            return;
        }

        profiles.add(newProfile);
        int oldSize = graph.length;
        int newSize = oldSize + 1;

        // Create new graph with +1 size
        boolean[][] newGraph = new boolean[newSize][newSize];
        for (int i = 0; i < oldSize; i++) {
            for (int j = 0; j < oldSize; j++) {
                newGraph[i][j] = graph[i][j];
            }
        }

        graph = newGraph; 
    }

    
    public void setFriend(Profile primaryUser, Profile friend, boolean areFriends) {
        // WRITE YOUR CODE HERE
        int idx1 = profiles.indexOf(primaryUser);
        int idx2 = profiles.indexOf(friend);

        // If either does not exist
        if (idx1 == -1 || idx2 == -1) {
            return;
        }
        graph[idx1][idx2] = areFriends;
        graph[idx2][idx1] = areFriends;

        
    } 

    public boolean searchFriend(Profile primaryUser, Profile friendLookup){
        // WRITE YOUR CODE HERE
        int idx1 = profiles.indexOf(primaryUser);
        int idx2 = profiles.indexOf(friendLookup);

        // If one is missing, can't be friends
        if (idx1 == -1 || idx2 == -1) {
            return false;
        }
        return graph[idx1][idx2]; 
    }
 
    public ArrayList<Profile> allFriends(Profile primaryUser){
        // WRITE YOUR CODE HERE
        ArrayList<Profile> result = new ArrayList<>();
        int idx = profiles.indexOf(primaryUser);

        // If profile doesn't exist, return empty
        if (idx == -1) {
            return result;
        }

        // Check all profiles for friendship
        for (int j = 0; j < graph.length; j++) {
            if (graph[idx][j]) {
                result.add(profiles.get(j));
            }
        }

        return result;
    }
}
