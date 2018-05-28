package webapp.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import webapp.models.Session;
import webapp.models.Trainee;
import webapp.models.Workout;

import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {

    List<Session> findByWorkoutAndTraineeAndIsCompleted(Workout workout, Trainee trainee, boolean isCompleted);
    List<Session> findByTraineeAndIsCompleted(Trainee trainee, boolean isCompleted);



}
