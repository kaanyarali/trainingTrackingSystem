package webapp.repos;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import webapp.models.Assesment;
import webapp.models.Trainee;
import webapp.models.Trainer;

import java.util.List;

public interface AssesmentRepository extends MongoRepository<Assesment, String> {

    List<Assesment> findByCreatedBy(Trainer trainer);
    List<Assesment> findByCreatedByAndTrainee(Trainer trainer, Trainee trainee,Sort sort);
    List<Assesment> findByTrainee(Trainee trainee);

}
