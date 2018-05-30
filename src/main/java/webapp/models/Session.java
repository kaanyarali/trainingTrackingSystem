package webapp.models;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Session {

    public interface PreviewView {};

    public interface DetailedView extends PreviewView {};
    @Id
    private String id;

    @DBRef(lazy=true)
    private Workout workout;
    @DBRef(lazy=true)
    private Trainee trainee;
    private ArrayList<WorkoutItem> workoutItems=new ArrayList<>();
    private boolean isCompleted;
    private boolean timeOut;
    private Date createdDate;
    private Date completeDate;
    private int completedRewardingPoint = 0;

    public Session(Workout workout, Trainee trainee){
        this.workout = workout;
        this.trainee = trainee;
        isCompleted=false;
        timeOut=false;
        createdDate=new Date();
    }

    @JsonView(PreviewView.class)
    public String getId() {
        return id;
    }

    public void addWorkoutItem(WorkoutItem workoutItem)
    {
        workoutItems.add(workoutItem);
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    @JsonView(PreviewView.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonView(DetailedView.class)
    public Workout getWorkout() {
        return workout;
    }

    @JsonView(DetailedView.class)
    public Trainee getTrainee() {
        return trainee;
    }

    @JsonView(DetailedView.class)
    public ArrayList<WorkoutItem> getWorkoutItems() {
        return workoutItems;
    }

    @JsonView(DetailedView.class)
    public boolean isCompleted() {
        return isCompleted;
    }

    @JsonView(DetailedView.class)
    public boolean isTimeOut() {
        return timeOut;
    }

    @JsonView(DetailedView.class)
    public Date getCompleteDate() {
        return completeDate;
    }

    @JsonView(DetailedView.class)
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    @JsonView(DetailedView.class)
    public int getCompletedRewardingPoint()
    {
        //Iterator<WorkoutItem> itemIterator=workoutItems.iterator();
       //while(itemIterator.hasNext())
       //{
           //completedRewardingPoint+=itemIterator.next().getRewardPoints();
         //  System.out.println(itemIterator.next().getMovement());
       //}
        return completedRewardingPoint;
    }
    @JsonView(DetailedView.class)
    public void setCompletedRewardingPoint() {
        Iterator<WorkoutItem> itemIterator=workoutItems.iterator();
        while(itemIterator.hasNext())
        {
            completedRewardingPoint+=itemIterator.next().getRewardPoints();
        }
    }
    @JsonView(DetailedView.class)
    public void showPoints()
    {
        int size=workout.getContent().size();
        for(int i=0;i<size;i++)
        {
            System.out.println(workout.getContent().get(i).getMovement());
            System.out.println(workout.getContent().get(i).getRewardPoints());
        }
    }
}
