package tree;

/**
 * Enumeration representing different types of bugs based on temperature ranges.
 * Each bug type has a corresponding name.
 * 
 * @author Ayla Muminovic
 * @author Christian Parra 
 */
public enum Bug {
    BUTTERFLY("Butterfly"), LADYBUG("Ladybug"), LANTERNFLY("Lanternfly"), EMPTY("Empty");

    private String name;

    private Bug(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public static Bug fromTemp(int temp) {
        if (temp < 10 || temp > 40) {
            return EMPTY; // temp out of range
        } else if (temp < 20) {
            return LADYBUG; // temp between 10 and 20 exclusive
        } else if (temp <= 30) {
            return LANTERNFLY; // temp between 20 and 30 inclusive
        } else {
            return BUTTERFLY; // temp between 31 and 40 inclusive
        }
    }

}
