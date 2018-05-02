package webapp.models;

public class WorkoutItem {

    private Movement movement;
    private Integer numSets;
    private Integer numRep;
    private Integer weight;
    private Integer rewardPoints;

    public WorkoutItem() {}

    public WorkoutItem(Movement movement, Integer sets, Integer reps, Integer weight, Integer rewardPoints) {
        this.movement = movement;
        this.numSets = sets;
        this.numRep = reps;
        this.weight = weight;
        this.rewardPoints = rewardPoints;
    }

    public Movement getMovement() {
        return movement;
    }

    public Integer getNumSets() {
        return numSets;
    }

    public Integer getNumRep() {
        return numRep;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }
}
