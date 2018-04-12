package webapp.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import webapp.models.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByFirstName(String name);

    List<User> findByUsername(String username);

}
