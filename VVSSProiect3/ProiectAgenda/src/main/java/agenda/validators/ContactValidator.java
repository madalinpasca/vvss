package agenda.validators;

public class ContactValidator {
    public static boolean isValidId(Long id) {
        return id >= 0;
    }

    public static boolean isValidName(String name) {
        return name.split("[\\p{Punct}\\s]+").length <= 2;
    }

    public static boolean isValidAddress(@SuppressWarnings("unused") String address) {
        return true;
    }

    public static boolean isValidPhone(String tel) {
        String[] s = tel.split("[!\"#$%&'()*,\\-./:;<=>?@\\[\\]\\\\^_`{|}~\\s]+");
        if (tel.charAt(0) == '+' && s.length == 2 ) {
            return true;
        }
        if (tel.charAt(0) != '0') {
            return false;
        }
        return s.length == 1;
    }

    public static boolean isValidEmail(String email) {
        return email.length() >= 1;
    }
}
