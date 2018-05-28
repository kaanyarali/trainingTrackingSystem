package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webapp.exception.NotFoundException;
import webapp.mappings.UserLoginMapping;
import webapp.mappings.UserSignupMapping;
import webapp.mappings.UserSearchMapping;
import webapp.models.*;
import webapp.repos.AssesmentRepository;
import webapp.repos.SessionRepository;
import webapp.repos.UserRepository;
import webapp.repos.WorkoutRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
public class LoginController {

    @Autowired
    private UserRepository repository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private AssesmentRepository assesmentRepository;

    @GetMapping(value = "/")
    public String index(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model) {
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            if (user.get().getRole().equals("trainee")) {
                return "redirect:trainee_home";
            } else {
                return "redirect:home";
            }
        }

        // Decide which video to use on the front p age
        Random rand = new Random();
        int randomNum = rand.nextInt(10);

        model.addAttribute("randomvideo", randomNum > 5);
        return "login";
    }


    @PostMapping(value = "/signin",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String signin(UserLoginMapping userMapping, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "login";
        }
        // Find user in db by username
        List<User> users = repository.findByUsername(userMapping.getUsername());
        if (users.size() == 0) {
            return "login";
        }
        User u = users.get(0);
        // Check user password correct
        if(!u.getPassword().equals(encryptPassword(userMapping.getPassword())))
        {
            return "login";
        }
        // Set cookie
        Cookie cookie = new Cookie("u_id",u.getId());
        response.addCookie(cookie);
        // Check user role, and redirect correspondingly to the home page
        if(u.getRole().equals("trainee"))
        {
            return "redirect:trainee_home";
        }
        return "redirect:home";
    }
    @PostMapping(value = "/home",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addTrainee(@CookieValue(value = "u_id")String userId , UserSearchMapping UserSearchMapping, BindingResult result,
                             HttpServletResponse response)
    {
        if (result.hasErrors()) {
            return "login";
        }
        Optional<User> trainers=repository.findById(userId);
        User kaan=trainers.get();
        if(kaan instanceof Trainer)
        {
            Trainer trainer=(Trainer) kaan;
            List<User> users =repository.findByUsername(UserSearchMapping.getUsername());
            if (users.size() == 0) {
                return "login";
            }
            User u = users.get(0);
            if(u instanceof Trainee)
            {
                Trainee trainee=(Trainee) u;
                Iterator<Trainee> iterator=trainer.getTraineeList().iterator();
                while(iterator.hasNext())
                {
                    if(iterator.next().getUsername().equals(u.getUsername()))
                    {
                        return "redirect:home";
                    }

                }
                if(trainee.getMyTrainer() !=null)
                {
                    trainee.getMyTrainer().deleteTrainee(trainee);
                    System.out.println(((Trainee) u).getMyTrainer().getUsername());
                    System.out.println(u.getUsername());
                    repository.save(trainee.getMyTrainer());
                }
                //System.out.println(u.getUsername());
                trainer.addTrainee(u);
                repository.save(trainer);
                ((Trainee) u).setMyTrainer(trainer);
                repository.save(u);
               // System.out.println("hello");
                return "redirect:home";
            }


        }

        return "redirect:home";
    }






    @RequestMapping(value="/trainee_home",method=RequestMethod.GET)
    public String homeTrainee(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model)  throws NotFoundException
    {
        Optional<User> u = repository.findById(userId);
        if (u.isPresent()) {
            model.addAttribute("user", u.get());
            List<Session> completedSessions=sessionRepository.findByTraineeAndIsCompleted((Trainee) u.get(),true);
            if(completedSessions.size()>0)
            {
                model.addAttribute("completedSessions",completedSessions);
            }
            return "trainee_home";
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value="/signout",method = RequestMethod.GET)
    public String signOut(HttpServletResponse response)
    {
       Cookie cookie=new Cookie("u_id",null);
       cookie.setMaxAge(0);
       response.addCookie(cookie);
       return "redirect:/";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup() {
        return "signup";
    }

    @RequestMapping(value= "/Assesment", method= RequestMethod.GET)
    public String changeProfile(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model)
    {
        Optional<User> userOptional=repository.findById(userId);
        Trainer trainer= (Trainer) userOptional.get();
        model.addAttribute("trainer",trainer);
        return "Assesment";
    }

    @RequestMapping(value= "/signup",method= RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String signupPost(UserSignupMapping userMapping, BindingResult result, HttpServletResponse response) throws NotFoundException
    {
        if(!userMapping.getPassword().equals(userMapping.getRePassword()))
        {
            return "redirect:signup";
        }

        if(userMapping.getUsername().equals(""))
        {
            throw new NotFoundException();
            //return "redirect:signup";
        }
        List<User> users =repository.findByUsername(userMapping.getUsername());
        if (users.size() > 0) {
            return "redirect:signup";
        }


        // encrypt password
        String encryptedPassword=encryptPassword(userMapping.getPassword());

        if(userMapping.getRole().equals("trainer")) {
           // Trainee kaan = new Trainee();
            //ArrayList<Trainee> mylist = new ArrayList<>();
            //mylist.add(kaan);
            Trainer user = new Trainer(userMapping.getUsername(), encryptedPassword, userMapping.getFirstName(),
                    userMapping.getLastName(), userMapping.getFitness(), userMapping.getEmail(), userMapping.getRole());
            repository.save(user);
            Cookie cookie = new Cookie("u_id", user.getId());

            response.addCookie(cookie);
        }
        else
        {
            Trainee user = new Trainee(userMapping.getUsername(), encryptedPassword, userMapping.getFirstName(),
                    userMapping.getLastName(), userMapping.getFitness(), userMapping.getEmail(), userMapping.getRole(),
                    null,0,0,0);
            repository.save(user);
            Cookie cookie = new Cookie("u_id", user.getId());

            response.addCookie(cookie);
            return "redirect:trainee_home";
        }
       //User user = new User(userMapping.getUsername(), encryptedPassword, userMapping.getFirstName(),
         //     userMapping.getLastName(), userMapping.getFitness(), userMapping.getEmail(), userMapping.getRole());
        // Save the created user to the db

        //repository.save(user);
        //Cookie cookie = new Cookie("u_id", user.getId());

        //response.addCookie(cookie);
        //if(user.getRole().equals("trainee"))
        //{
          //  return "redirect:trainee_home";
        //}
        return "redirect:home";

    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model)
            throws NotFoundException {

        Optional<User> user=repository.findById(userId);
        List<Workout> workouts=workoutRepository.findByCreatedBy(user.get());
        List<Assesment> assessments=assesmentRepository.findByCreatedBy((Trainer) user.get());

        if(user.isPresent())
        {
            model.addAttribute("user",user.get());
            model.addAttribute("trainer",(Trainer) user.get());
            model.addAttribute("workouts",workouts);
            model.addAttribute("assessments",filteredAssesmentList(assessments));

            return "home";
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/select_trainee", method = RequestMethod.GET)
    public String selectTrainee(@CookieValue(value = "u_id", defaultValue = "") Model model)
            throws NotFoundException {

        List<User> user=repository.findAll();
        model.addAttribute("user", user.get(0));

        return "select_trainee";
    }




    private static String encryptPassword(String password) {
        /**
         * Encrypts the password passed to it using MD5 algorithm
         * @param password: Password to encrypt
         * @returns: Encrypted password./gradlew build --continuous
         **/
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String encryptUserCookie(User user) {
        return "";
    }

    private static String decryptyUserCookie(String cookieValue) {
        return "";
    }
    private ArrayList<Assesment> filteredAssesmentList(List<Assesment> assesments)
    {
        HashMap<String,Assesment> hashMap=new HashMap<>();
        ArrayList<Assesment> assesmentList =new ArrayList<>();
        int listAssessmentSize=assesments.size();
        for(int i=0;i<listAssessmentSize;i++)
        {
            if(!hashMap.containsKey(assesments.get(i).getTrainee()))
            {
                hashMap.put(assesments.get(i).getTrainee().getId(),assesments.get(i));
            } else if(assesments.get(i).getCreatedDate().after(hashMap.get(assesments.get(i).getTrainee().getId()).getCreatedDate()))
            {
                hashMap.put(assesments.get(i).getTrainee().getId(),assesments.get(i));
            }
        }
        for (Map.Entry<String, Assesment> entry : hashMap.entrySet()) {
            assesmentList.add(entry.getValue());
        }
        return assesmentList;
    }
}
