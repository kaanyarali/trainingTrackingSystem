package webapp.mappings;

public class UserLoginMapping {

    private final String username;
    private final String password;

    public UserLoginMapping(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
