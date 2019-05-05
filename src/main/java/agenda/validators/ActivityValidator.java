package agenda.validators;

import java.util.Date;

public class ActivityValidator {
    public static boolean isValidId(Long id) {
        return id >= 0;
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

    public static boolean isValidStart(@SuppressWarnings("unused") Date start) {
        return true;
    }

    public static boolean isValidDuration(@SuppressWarnings("unused") Long duration) {
        return true;
    }

    public static boolean isValidDescription(String description) {
        return description != null && description.length() >= 3;
    }
}
