package agenda.validators;

public class UserValidator {
    public static boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
}
