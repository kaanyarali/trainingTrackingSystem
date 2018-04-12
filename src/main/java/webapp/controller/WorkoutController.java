package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import webapp.models.Movement;
import webapp.models.User;
import webapp.models.Workout;
import webapp.repos.UserRepository;
import webapp.repos.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/workouts")
    public void getWorkouts(@CookieValue(value = "u_id") String userId, Model model) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            /*
            Workout w = new Workout("test workout", userOptional.get());
            w.addItem(Movement.BENCH_PRESS, 3, 12);
            workoutRepository.save(w);
            */

            List<Workout> workouts = workoutRepository.findByCreatedBy(userOptional.get());
            model.addAttribute("workouts", workouts);
        }
    }



}
