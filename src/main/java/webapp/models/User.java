package webapp.models;

public class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String fitnessCenter;
    private String email;
    private String role;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password,String firstName,String lastName,String fitnessCenter,
                String email,String role) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
