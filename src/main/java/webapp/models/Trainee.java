package webapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

@Document(collection="user")
public class Trainee extends User {

    @DBRef(lazy=true)
    private Trainer myTrainer;
    private int height;
    private int weight;
    private int age;


    private Date lastSessionDate;
    @DBRef(lazy=true)
    private ArrayList<Workout> workouts = new ArrayList<>();

    public Trainee(String username, String password, String firstName, String lastName, String fitnessCenter,
                   String email, String role, Trainer myTrainer, int height, int weight, int age)
    {
        super(username, password, firstName, lastName, fitnessCenter, email, role);
        this.myTrainer = myTrainer;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.lastSessionDate=null;
    }

    public void addWorkout(Workout workout)
    {
        workouts.add(workout);
    }

    public ArrayList<Workout>  getWorkouts() {
        return workouts;
    }

    public Trainer getMyTrainer() {
        return myTrainer;
    }

    public void setMyTrainer(Trainer myTrainer) {
        this.myTrainer = myTrainer;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public Trainee()
    {

    }
    public Date getLastSessionDate() {
        return lastSessionDate;
    }
    public void setLastSessionDate(Date lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }

    public String getLastSessionDateFormatted() {
        return DateFormat.getDateInstance(DateFormat.LONG).format(getLastSessionDate());
    }

}
