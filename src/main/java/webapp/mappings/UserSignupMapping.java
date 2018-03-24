package webapp.mappings;

public class UserSignupMapping extends UserLoginMapping {

    private String email;
    private String firstName;
    private String lastName;
    private String fitness;
    private String role;
    private String rePassword;

    public UserSignupMapping(String firstName, String lastName, String email, String fitness, String username,
                             String password, String role, String rePassword)
    {
        super(username,password);
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.fitness=fitness;
        this.role = role;
        this.rePassword = rePassword;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFitness() {
        return fitness;
    }

    public String getRole() {
        return role;
    }

    public String getRePassword() {
        return rePassword;
    }
}
