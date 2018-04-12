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
import webapp.models.User;
import webapp.repos.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    private UserRepository repository;

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

        // Decide which video to use on the front page
        Random rand = new Random();
        int randomNum = rand.nextInt(10);

        repository.findAll();

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

    @RequestMapping(value="/trainee_home",method=RequestMethod.GET)
    public String homeTrainee(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model)  throws NotFoundException
    {
        Optional<User> u = repository.findById(userId);
        if (u.isPresent()) {
            model.addAttribute("user", u.get());
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

    @RequestMapping(value= "/signup",method= RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String signupPost(UserSignupMapping userMapping, BindingResult result, HttpServletResponse response)
    {
        if(!userMapping.getPassword().equals(userMapping.getRePassword()))
        {
            return "redirect:signup";
        }

        if(userMapping.getUsername().equals(""))
        {
            return "redirect:signup";
        }
        List<User> users =repository.findByUsername(userMapping.getUsername());
        if (users.size() > 0) {
            return "redirect:signup";
        }

        // encrypt password
        String encryptedPassword=encryptPassword(userMapping.getPassword());

        User user = new User(userMapping.getUsername(), encryptedPassword, userMapping.getFirstName(),
                userMapping.getLastName(), userMapping.getFitness(), userMapping.getEmail(), userMapping.getRole());
        // Save the created user to the db
        repository.save(user);
        Cookie cookie = new Cookie("u_id", user.getId());

        response.addCookie(cookie);
        if(user.getRole().equals("trainee"))
        {
            return "redirect:trainee_home";
        }
        return "redirect:home";
    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@CookieValue(value = "u_id", defaultValue = "") String userId, Model model)
            throws NotFoundException {

        Optional<User> user=repository.findById(userId);

        if(user.isPresent())
        {
            model.addAttribute("user",user.get());
            return "home";
        } else {
            throw new NotFoundException();
        }
    }

    private static String encryptPassword(String password) {
        /**
         * Encrypts the password passed to it using MD5 algorithm
         * @param password: Password to encrypt
         * @returns: Encrypted password
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
}
