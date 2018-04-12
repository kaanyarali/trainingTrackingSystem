package webapp.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import webapp.models.User;
import webapp.models.Workout;

import java.util.List;

public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByCreatedBy(User user);
}
