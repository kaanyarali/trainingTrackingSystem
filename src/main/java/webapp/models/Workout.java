package webapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class Workout {

    @Id
    private String id;
    private String name;

    private int duration;

    @DBRef
    private User createdBy; // Trainee who has created

    private List<WorkoutItem> content;

    public Workout() {
    }

    public Workout(String name, int duration, User createdBy) {
        this.name = name;
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

    public int getDuration() {
        return duration;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<WorkoutItem> getContent() {
        return content;
    }
}
