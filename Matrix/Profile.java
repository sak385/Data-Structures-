package graph;

import edu.rutgers.cs112.Comparable112;

public class Profile extends Comparable112<Profile> {

    private String username;
    private String name;
    private String status; 
 
    public Profile(String username, String name, String status){
        if (username == null || name == null || status == null){
            throw new IllegalArgumentException("Profile parameters cannot be null");
        }
        this.username = username;
        this.name = name;
        this.status = status;
    }

    public String getUsername(){
        return this.username;
    }

    public String getName(){
        return this.name;
    }

    public String getStatus(){
        return this.status;
    } 

    @Override
    public int compareTo(Profile arg0) {
        int userComp = this.username.compareTo(arg0.username);
        int nameComp = this.name.compareTo(arg0.name);
        if(userComp != 0){
            return userComp;
        } else {
            return nameComp;
        }
    }

    @Override
    public boolean equals(Object arg0) {
        if(!(arg0 instanceof Profile)){
            return false;
        }
        Profile other = (Profile) arg0;
        return this.username.equals(other.username) && this.name.equals(other.name);     
    }

    @Override
    public String toString() {
        return "Username: " + this.username + ", Name: " + this.name + ", Status: " + this.status;
    }



}
