package webapp.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import webapp.exception.NotFoundException;
import webapp.mappings.UserLoginMapping;
import webapp.mappings.UserSignupMapping;
import webapp.models.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class LoginController {

    private final AtomicInteger counter = new AtomicInteger();
    private final Hashtable<String,User> userRepo=new Hashtable<>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@CookieValue(value = "username", defaultValue = "") String username) {
        if (userRepo.containsKey(username)) {
            User user = userRepo.get(username);

            if (user.getRole().equals("trainee")) {
                return "redirect:trainee_home";
            } else {
                return "redirect:home";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String signin(UserLoginMapping userMapping, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "login";
        }
        if(!userRepo.containsKey(userMapping.getUsername()) ||
                !userRepo.get(userMapping.getUsername()).getPassword().equals(encryptPassword(userMapping.getPassword())))
        {
            return "login";
        }
        User a=userRepo.get(userMapping.getUsername());
        Cookie cookie = new Cookie("username",userMapping.getUsername());
        response.addCookie(cookie);
        if(a.getRole().equals("trainee"))
        {
            return "redirect:trainee_home";
        }
        return "redirect:home";
    }
    @RequestMapping(value="/trainee_home",method=RequestMethod.GET)
    public String homeTrainee(@CookieValue(value = "username", defaultValue = "") String username, Model model)  throws NotFoundException
    {
        if (userRepo.containsKey(username)) {
            model.addAttribute("user", userRepo.get(username));
            return "trainee_home";
        } else {
            throw new NotFoundException();
        }
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
        if(userMapping.getUsername().equals("") || userRepo.containsKey(userMapping.getUsername()))
        {
            return "redirect:signup";
        }

        String encryptedPassword=encryptPassword(userMapping.getPassword());

        User a=new User(counter.incrementAndGet(),userMapping.getUsername(),encryptedPassword,userMapping.getFirstName(),
                userMapping.getLastName(),userMapping.getFitness(),userMapping.getEmail(),userMapping.getRole());
        userRepo.put(a.getUsername(),a);
        Cookie cookie=new Cookie("username",a.getUsername());

        response.addCookie(cookie);
        return "redirect:home";
    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@CookieValue(value = "username", defaultValue = "") String username, Model model)
            throws NotFoundException {
        if (userRepo.containsKey(username)) {
            model.addAttribute("user", userRepo.get(username));
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
