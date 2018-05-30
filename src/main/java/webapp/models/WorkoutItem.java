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
        this.numSets = sets != null ? sets : 0;
        this.numRep = reps != null ? reps : 0;
        this.weight = weight != null ? weight : 0;
        this.rewardPoints = rewardPoints != null ? rewardPoints : 0;
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
