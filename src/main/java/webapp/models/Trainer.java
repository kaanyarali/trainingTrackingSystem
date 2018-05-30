package webapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection="user")
public class Trainer extends User {


    @DBRef(lazy=true)
    private ArrayList<Trainee> traineeList;

    public Trainer(String username, String password, String firstName, String lastName, String fitnessCenter,
                   String email, String role)
    {
        super(username, password, firstName, lastName, fitnessCenter, email, role);
        this.traineeList=new ArrayList<>();
    }


    public ArrayList<Trainee> getTraineeList() {
        return traineeList;
    }
    public void addTrainee(User user)
    {
        this.traineeList.add((Trainee) user);
    }
    public void deleteTrainee(User user)
    {
        for(int i=0;i<traineeList.size();i++)
        {
            if(traineeList.get(i).getUsername().equals(user.getUsername()))
            {
                this.traineeList.remove(i);
                break;
            }
        }
        return;
    }
}

