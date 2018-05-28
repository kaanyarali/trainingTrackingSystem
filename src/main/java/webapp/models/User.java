package webapp.models;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    protected String id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String fitnessCenter;
    protected String email;
    protected String role;
    protected String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password,String firstName,String lastName,String fitnessCenter,
                String email,String role) {
        this.username = username;
        this.password = password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.fitnessCenter=fitnessCenter;
        this.email=email;
        this.role=role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullname() {
        return getFirstName() + " " + getLastName();
    }

    public String getFitnessCenter() {
        return fitnessCenter;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
