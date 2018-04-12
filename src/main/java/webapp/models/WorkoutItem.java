package webapp.models;

public class WorkoutItem {

    private Movement movement;
    private int numSets;
    private int numRep;

    public WorkoutItem() {}

    public WorkoutItem(Movement movement, int sets, int reps) {
        this.movement = movement;
        this.numSets = sets;
        this.numRep = reps;
    }

    public Movement getMovement() {
        return movement;
    }

    public int getNumSets() {
        return numSets;
    }

    public int getNumRep() {
        return numRep;
    }
}
