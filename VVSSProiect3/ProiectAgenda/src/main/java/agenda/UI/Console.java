package agenda.UI;

import agenda.model.Activity;
import agenda.model.Contact;
import agenda.service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Console {
    private final Service service;
    private boolean loggedIn = false;
    private Scanner scanner = new Scanner(System.in);

    public Console (Service service) {
        this.service = service;
    }

    public void show() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (loggedIn) {
                showLogged();
            } else {
                showNotLogged();
            }
        }
    }

    private void showNotLogged() {
        int option = showNotLoggedOption();
        switch (option) {
            case 1:
                showLogIn();
                break;
            case 2:
                showRegister();
                break;
            case 0:
            default:
                System.exit(0);
        }
    }

    private void showRegister() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Password again: ");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            System.out.println("Password do not match!");
            return;
        }
        try {
            service.register(name, username, password);
        } catch (Exception e) {
            System.out.println("User registration failed!");
            e.printStackTrace();
            return;
        }
        System.out.println("User registered!");
    }

    private void showLogIn() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (service.login(username, password)) {
            loggedIn = true;
            return;
        }
        System.out.println("Wrong username or password!");
    }

    private int showNotLoggedOption() {
        System.out.println("-----------------------------");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Option= ");
        try {
            int option = readInt();
            if (option < 0 || option > 2)
                throw new Exception();
            return option;
        } catch (Exception ex) {
            System.out.println("Bad option!");
            return showNotLoggedOption();
        }
    }

    private int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    private void showLogged() {
        int option = showLoggedOption();
        switch (option) {
            case 1:
                showAddContact();
                break;
            case 2:
                showAddActivity();
                break;
            case 3:
                showDateActivities();
                break;
            case 0:
            default:
                loggedIn = false;
                break;
        }
    }

    private void showDateActivities() {
        System.out.println("Date (dd-mm-yyyy): ");
        Date date;
        try {
            date = readDate();
        } catch (Exception ex) {
            System.out.println("Bad date format!");
            return;
        }
        List<Activity> activities = service.getActivities(date);
        if (activities.size() == 0) {
            System.out.println("Nothing planned for that date!");
            return;
        }
        System.out.println("Planned activities: ");
        for (int i = 0, activitiesSize = activities.size(); i < activitiesSize; i++) {
            Activity activity = activities.get(i);
            System.out.println(i + ". " + activity);
        }
    }

    private Date readDate() throws ParseException {
        return new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).parse(scanner.nextLine());
    }

    private void showAddActivity() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Start (dd-mm-yyyy HH:MM): ");
        Date start;
        try {
            start = readDateAndTime();
        } catch (Exception ex) {
            System.out.println("Bad date format!");
            return;
        }
        System.out.print("Duration (HH:MM): ");
        Long duration;
        try {
            duration = readTime();
        } catch (Exception ex) {
            System.out.println("Bad time format!");
            return;
        }
        System.out.print("Description: ");
        String description = scanner.nextLine();
        List<Contact> contacts = readContacts();
        try {
            service.addActivity(name, start, duration, description, contacts);
            System.out.println("Activity added!");
        } catch (Exception e) {
            System.out.println("Adding activity failed");
            e.printStackTrace();
        }
    }

    private List<Contact> readContacts() {
        List<Contact> availableContacts = service.getContacts();
        List<Contact> selectedContacts = new ArrayList<>();
        while (true) {
            int option = 0;
            if (availableContacts.size() > 0) {
                showAvailableContacts(availableContacts);
                try {
                    option = readInt();
                    if (option < 0 || option > availableContacts.size())
                        throw new Exception();
                } catch (Exception ex) {
                    System.out.println("Bad option! Try again");
                    continue;
                }
            }
            if (option == 0)
                break;
            Contact contact = availableContacts.get(option - 1);
            selectedContacts.add(contact);
            availableContacts.remove(option - 1);
            System.out.println("Contact selected!");
        }
        return selectedContacts;
    }

    private void showAvailableContacts(List<Contact> availableContacts) {
        System.out.println("Select contacts:");
        for (int i = 0, availableContactsSize = availableContacts.size(); i < availableContactsSize; i++) {
            Contact contact = availableContacts.get(i);
            System.out.println((i + 1) + ". " + contact);
        }
        System.out.println("0. Done");
        System.out.print("Option: ");
    }

    private Long readTime() throws ParseException {
        String timeRaw = scanner.nextLine();
        String[] slit = timeRaw.split(":");
        if (slit.length != 2 ||
                slit[0].length() < 1 || slit[0].length() > 2 ||
                slit[1].length() < 1 || slit[1].length() > 2)
            throw new ParseException("Time parse error!", 2);
        long time = 0L;
        try {
            time += Long.parseLong(slit[0]) * 3600000L;
        } catch (NumberFormatException ex) {
            throw new ParseException("Time parse error!", 0);
        }
        try {
            long minutes = Long.parseLong(slit[1]);
            if (minutes < 0 || minutes > 59)
                throw new ParseException("Time parse error!", 3);
            time += minutes * 60000L;
        } catch (NumberFormatException ex) {
            throw new ParseException("Time parse error!", 3);
        }
        return time;
    }

    private Date readDateAndTime() throws ParseException {
       return new SimpleDateFormat("dd-MM-yyy HH:mm", Locale.getDefault()).parse(scanner.nextLine());
    }

    private void showAddContact() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        try {
            service.addContact(name,address,phone,email);
            System.out.println("Contact added!");
        } catch (Exception e) {
            System.out.println("Adding contact failed");
            e.printStackTrace();
        }
    }

    private int showLoggedOption() {
        System.out.println("-----------------------------");
        System.out.println("1. Add contact");
        System.out.println("2. Add activity");
        System.out.println("3. Display activities");
        System.out.println("0. Logout");
        System.out.print("Option= ");
        try {
            int option = readInt();
            if (option < 0 || option > 3)
                throw new Exception();
            return option;
        } catch (Exception ex) {
            System.out.println("Bad option!");
            return showNotLoggedOption();
        }
    }
}
