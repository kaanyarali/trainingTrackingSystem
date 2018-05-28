package webapp.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import webapp.exception.NotFoundException;
import webapp.mappings.WorkoutItemMapping;
import webapp.models.*;
import webapp.repos.SessionRepository;
import webapp.repos.UserRepository;
import webapp.repos.WorkoutRepository;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Date;
import java.util.Optional;

@Controller
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkoutRepository workoutRepository;

/*
    @GetMapping(value = "/workouts/{workoutId}/session")
    public String createSession1(@PathVariable String workoutId, @CookieValue(value = "u_id") String userId) throws Exception {
        WorkoutItem workoutItem = new WorkoutItem(Movement.BENCH_PRESS, 3, 3, 10, 100);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Workout> workoutOptional = workoutRepository.findById(workoutId);
            if (workoutOptional.isPresent()) {
                Workout workout = workoutOptional.get();
                if (user instanceof Trainee) {
                    Trainee trainee = (Trainee) user;
                    Session s = new Session(workout, trainee);
                    s.addWorkoutItem(workoutItem);
                    sessionRepository.save(s);
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
        return "trainee_home";
    }

    */

    @PostMapping(value = "/workouts/{workoutId}/session")
    @JsonView(Session.PreviewView.class)
    @ResponseBody
    public Session createSession(@PathVariable String workoutId,
                                 @CookieValue(value = "u_id") String userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Workout> workoutOptional = workoutRepository.findById(workoutId);
            if (workoutOptional.isPresent()) {
                Workout workout = workoutOptional.get();
                if (user instanceof Trainee) {
                    Trainee trainee = (Trainee) user;
                    Session s = new Session(workout, trainee);
                    sessionRepository.save(s);
                    return s;
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    @PostMapping(value = "/workouts/{workoutId}/session/{sessionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Session.DetailedView.class)
    @ResponseBody
    public Session addWorkoutItem(@PathVariable String workoutId, @PathVariable String sessionId,
                                  @CookieValue(value = "u_id") String userId,
                                  @RequestBody WorkoutItemMapping workoutItemMapping) throws Exception {
        WorkoutItem workoutItem = new WorkoutItem(workoutItemMapping.getMovement(), workoutItemMapping.getNumSets(), workoutItemMapping.getNumRep(),
                workoutItemMapping.getWeight(), workoutItemMapping.getRewardPoints());
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            Session s = optionalSession.get();
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user instanceof Trainee) {
                    Trainee trainee = (Trainee) user;
                    if (s.getTrainee().getId().equals(trainee.getId())) {
                        s.addWorkoutItem(workoutItem);
                        sessionRepository.save(s);
                        return s;
                    }
                }
            }
        } else {
            throw new NotFoundException();
        }
        return null;
    }

    @PostMapping(value = "/workouts/{workoutId}/session/{sessionId}/complete")
    @JsonView(Session.PreviewView.class)
    @ResponseBody
    public Session completeSession(@PathVariable String workoutId, @PathVariable String sessionId,
                                   @CookieValue(value = "u_id") String userId) throws Exception
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Workout> workoutOptional = workoutRepository.findById(workoutId);
            if (workoutOptional.isPresent()) {
                Workout workout = workoutOptional.get();
                if (user instanceof Trainee) {
                    Trainee trainee = (Trainee) user;
                    Optional<Session> optionalSession = sessionRepository.findById(sessionId);
                    if (optionalSession.isPresent()) {
                        Session s = optionalSession.get();
                        s.setCompleted(true);
                        Date newdate = new Date();
                        s.setCompleteDate(newdate);
                        trainee.setLastSessionDate(newdate);
                        userRepository.save(trainee);
                        sessionRepository.save(s);
                        return s;
                    } else {
                        throw new NotFoundException();
                    }
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new NotFoundException();
            }
        }
        return null;
    }
}
