package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webapp.exception.BadRequestException;
import webapp.exception.ForbiddenException;
import webapp.exception.NotFoundException;
import webapp.mappings.UserSearchMapping;
import webapp.mappings.UserSignupMapping;
import webapp.mappings.WorkoutMapping;
import webapp.models.*;
import webapp.repos.SessionRepository;
import webapp.repos.UserRepository;
import webapp.repos.WorkoutRepository;

import java.util.List;
import java.util.Map;
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
    public String createWorkouts(WorkoutMapping workoutMapping, BindingResult result,
                                 @CookieValue(value = "u_id") String userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (user.get().getRole().equals("trainer")) {
                if (!result.hasErrors()) {
                    Workout w = fromWorkoutMapping(workoutMapping, user.get());
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

    private static Workout fromWorkoutMapping(WorkoutMapping workoutMapping, User user) {
        Workout w = new Workout(workoutMapping.getWorkoutName(), workoutMapping.getDescription(),
                workoutMapping.getWorkoutDuration(), user);
        if (workoutMapping.getSets0() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove0()), workoutMapping.getSets0(),
                    workoutMapping.getReps0(),
                    workoutMapping.getWeight0(),
                    workoutMapping.getRewards0());
        }

        if (workoutMapping.getSets1() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove1()), workoutMapping.getSets1(),
                    workoutMapping.getReps1(),
                    workoutMapping.getWeight1(),
                    workoutMapping.getRewards1());
        }

        if (workoutMapping.getSets2() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove2()), workoutMapping.getSets2(),
                    workoutMapping.getReps2(),
                    workoutMapping.getWeight2(),
                    workoutMapping.getRewards2());
        }

        if (workoutMapping.getSets3() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove3()), workoutMapping.getSets3(),
                    workoutMapping.getReps3(),
                    workoutMapping.getWeight3(),
                    workoutMapping.getRewards3());
        }

        if (workoutMapping.getSets4() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove4()), workoutMapping.getSets4(),
                    workoutMapping.getReps4(),
                    workoutMapping.getWeight4(),
                    workoutMapping.getRewards4());
        }

        if (workoutMapping.getSets5() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove5()), workoutMapping.getSets5(),
                    workoutMapping.getReps5(),
                    workoutMapping.getWeight5(),
                    workoutMapping.getRewards5());
        }

        if (workoutMapping.getSets6() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove6()), workoutMapping.getSets6(),
                    workoutMapping.getReps6(),
                    workoutMapping.getWeight6(),
                    workoutMapping.getRewards6());
        }

        if (workoutMapping.getSets7() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove7()), workoutMapping.getSets7(),
                    workoutMapping.getReps7(),
                    workoutMapping.getWeight7(),
                    workoutMapping.getRewards7());
        }

        if (workoutMapping.getSets8() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove8()), workoutMapping.getSets8(),
                    workoutMapping.getReps8(),
                    workoutMapping.getWeight8(),
                    workoutMapping.getRewards8());
        }

        if (workoutMapping.getSets9() != null) {
            w.addItem(Movement.valueOf(workoutMapping.getMove9()), workoutMapping.getSets9(),
                    workoutMapping.getReps9(),
                    workoutMapping.getWeight9(),
                    workoutMapping.getRewards9());
        }

        return w;
    }
}
