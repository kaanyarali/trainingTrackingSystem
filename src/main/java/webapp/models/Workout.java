package webapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Workout {

    @Id
    private String id;
    private String name;

    private int duration;

    @DBRef
    private User createdBy; // Trainer who has created

    private List<WorkoutItem> content;

    private String description;

    public Workout() {
    }

    public Workout(String name, String description, int duration, User createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.duration = duration;
        this.content = new ArrayList<>();
    }

    public void addItem(Movement movement, Integer sets, Integer reps, Integer weight, Integer rewardPoints) {
        content.add(new WorkoutItem(movement, sets, reps, weight, rewardPoints));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<WorkoutItem> getContent() {
        return content;
    }

    public int getRewardingPoint()
    {
        int rewardingPoint=0;
        Iterator<WorkoutItem> iterator = content.iterator();
        while(iterator.hasNext())
        {
            rewardingPoint=rewardingPoint+iterator.next().getRewardPoints();
        }
        return rewardingPoint;
    }

    public String getThoughness() {
        if (getRewardingPoint() >= 0 && getRewardingPoint() < 25) {
            return "warning";
        } else if (getRewardingPoint() >= 25 && getRewardingPoint() < 50) {
            return "primary";
        } else if (getRewardingPoint() >= 50 && getRewardingPoint() < 75) {
            return "success";

        } else {
            return "danger";
        }
    }
}
