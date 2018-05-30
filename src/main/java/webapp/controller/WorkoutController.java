package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webapp.exception.BadRequestException;
import webapp.exception.ForbiddenException;
import webapp.exception.NotFoundException;
import webapp.mappings.UserSearchMapping;
import webapp.mappings.WorkoutMapping;
import webapp.models.*;
import webapp.repos.SessionRepository;
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
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping(value = "/workouts")
    public void getWorkouts(@CookieValue(value = "u_id") String userId, Model model) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {

            List<Workout> workouts = workoutRepository.findByCreatedBy(userOptional.get());
            model.addAttribute("workouts", workouts);
        }
    }

    @PostMapping(value = "/workouts",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createWorkouts(@RequestBody MultiValueMap<String, String> map, BindingResult result,
                                 @CookieValue(value = "u_id") String userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (user.get().getRole().equals("trainer")) {
                if (!result.hasErrors()) {

                    Workout w = fromWorkoutMapping(map, user.get());
                    workoutRepository.save(w);
                    return "redirect:/workouts";
                }
                throw new BadRequestException();
            }
        }
        throw new ForbiddenException();
    }

    @GetMapping(value = "/workouts/{workoutId}")
    public String assign(@PathVariable String workoutId,@CookieValue(value = "u_id") String userId,Model model) throws Exception
    {
        Optional<Workout> workoutOptional=workoutRepository.findById(workoutId);
        Optional<User> optionalUser=userRepository.findById(userId);

        if(!workoutOptional.isPresent())
        {
            throw new NotFoundException();
        }
         else
        {
            Workout workout = workoutOptional.get();
            model.addAttribute("workout", workout);
            if(optionalUser.isPresent())
            {
                User user=optionalUser.get();
                model.addAttribute("user",user);
                if(user.getRole().equals("trainee"))
                {
                    List<Session> activeSessions = sessionRepository.findByWorkoutAndTraineeAndIsCompleted(workout, (Trainee)user, false);
                    if (activeSessions.size() == 1) {
                        model.addAttribute("session_count", 1);
                        model.addAttribute("workout_session_id", activeSessions.get(0).getId());
                        model.addAttribute("workout_session", activeSessions.get(0));
                    } else {
                        model.addAttribute("session_count", 0);
                        model.addAttribute("workout_session_id", -1);
                    }
                }

            }
            else
            {
                throw new NotFoundException ();
            }
        }

        return "workoutDetails";
    }

    @PostMapping(value = "/workouts/{workoutId}/assign")
     public String assignWorkout(@PathVariable String workoutId, @CookieValue(value = "u_id") String userId,
                                 UserSearchMapping userSearchMapping) throws Exception {
        Optional<User> trainers = userRepository.findById(userId);
        User kaan = trainers.get();
        if (kaan instanceof Trainer) {
            Trainer trainer = (Trainer) kaan;
            List<User> users = userRepository.findByUsername(userSearchMapping.getUsername());
            if (users.size() == 0) {
                throw new NotFoundException();
            }
            User u = users.get(0);
            if (u instanceof Trainee) {
                Trainee trainee = (Trainee) u;
                Optional<Workout> workoutOptional=workoutRepository.findById(workoutId);
                if(workoutOptional.isPresent())
                {
                    Workout workout=workoutOptional.get();
                    trainee.addWorkout(workout);
                    userRepository.save(trainee);
                    return "redirect:/workouts/"+workoutId;
                }
                else
                    throw new NotFoundException ();

            }

        }
        return "login";
    }

    private static Workout fromWorkoutMapping(MultiValueMap<String, String> map, User user) {
        Workout w = new Workout(map.getFirst("workoutName"), map.getFirst("description"),
                Integer.parseInt(map.getFirst("workoutDuration")), user);
        if (map.get("sets0") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move0")), Integer.parseInt(map.getFirst("sets0")),
                    Integer.parseInt(map.getFirst("reps0")),
                    Integer.parseInt(map.getFirst("weight0")),
                    Integer.parseInt(map.getFirst("rewards0")));
        }

        if (map.get("sets1") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move1")), Integer.parseInt(map.getFirst("sets1")),
                    Integer.parseInt(map.getFirst("reps1")),
                    Integer.parseInt(map.getFirst("weight1")),
                    Integer.parseInt(map.getFirst("rewards1")));
        }

        if (map.get("sets2") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move2")), Integer.parseInt(map.getFirst("sets2")),
                    Integer.parseInt(map.getFirst("reps2")),
                    Integer.parseInt(map.getFirst("weight2")),
                    Integer.parseInt(map.getFirst("rewards2")));
        }

        if (map.get("sets3") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move3")), Integer.parseInt(map.getFirst("sets3")),
                    Integer.parseInt(map.getFirst("reps3")),
                    Integer.parseInt(map.getFirst("weight3")),
                    Integer.parseInt(map.getFirst("rewards3")));
        }

        if (map.get("sets4") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move4")), Integer.parseInt(map.getFirst("sets4")),
                    Integer.parseInt(map.getFirst("reps4")),
                    Integer.parseInt(map.getFirst("weight4")),
                    Integer.parseInt(map.getFirst("rewards4")));
        }

        if (map.get("sets5") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move5")), Integer.parseInt(map.getFirst("sets5")),
                    Integer.parseInt(map.getFirst("reps5")),
                    Integer.parseInt(map.getFirst("weight5")),
                    Integer.parseInt(map.getFirst("rewards5")));
        }

        if (map.get("sets6") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move6")), Integer.parseInt(map.getFirst("sets6")),
                    Integer.parseInt(map.getFirst("reps6")),
                    Integer.parseInt(map.getFirst("weight6")),
                    Integer.parseInt(map.getFirst("rewards6")));
        }

        if (map.get("sets7") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move7")), Integer.parseInt(map.getFirst("sets7")),
                    Integer.parseInt(map.getFirst("reps7")),
                    Integer.parseInt(map.getFirst("weight7")),
                    Integer.parseInt(map.getFirst("rewards7")));
        }

        if (map.get("sets8") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move8")), Integer.parseInt(map.getFirst("sets8")),
                    Integer.parseInt(map.getFirst("reps8")),
                    Integer.parseInt(map.getFirst("weight8")),
                    Integer.parseInt(map.getFirst("rewards8")));
        }

        if (map.get("sets9") != null) {
            w.addItem(Movement.valueOf(map.getFirst("move9")), Integer.parseInt(map.getFirst("sets9")),
                    Integer.parseInt(map.getFirst("reps9")),
                    Integer.parseInt(map.getFirst("weight9")),
                    Integer.parseInt(map.getFirst("rewards9")));
        }
        return w;
    }
}
