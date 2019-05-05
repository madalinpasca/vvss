package agenda.service;

import agenda.model.Activity;
import agenda.model.Contact;
import agenda.model.User;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;
import agenda.repository.interfaces.IUserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final IUserRepository userRepository;
    private final IActivityRepository activityRepository;
    private final IContactRepository contactRepository;
    private User currentUser = null;

    public Service(IUserRepository userRepository,
                   IActivityRepository activityRepository,
                   IContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.contactRepository = contactRepository;
    }

    public boolean login(String username, String password) {
        User user = userRepository.get(username);
        if (user == null || !user.getPassword().equals(password))
            return false;
        currentUser = user;
        return true;
    }

    public void addContact(String name, String address, String phone, String email) {
        User user = useCurrentUser();
        Contact contact = new Contact(contactRepository.getNewId(), name, address, phone, email);
        contactRepository.addContact(contact);
        user.getContacts().add(contact);
        userRepository.update(user);
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(useCurrentUser().getContacts());
    }

    public void addActivity(String name, Date start, Long duration, String description, List<Contact> contacts) {
        User user = useCurrentUser();
        Activity activity = new Activity(activityRepository.getNewId(),
                name,
                start,
                duration,
                description,
                contacts);
        activityRepository.addActivity(activity);
        user.getActivities().add(activity);
        userRepository.update(user);
    }

    public List<Activity> getActivities(Date date) {
        return useCurrentUser()
                .getActivities()
                .stream()
                .filter(x->x.hasSameDate(date))
                .sorted(Comparator.comparing(Activity::getStart))
                .collect(Collectors.toList());
    }

    public void register(String name, String username, String password) {
        User user = new User(name, username, password, null, null);
        userRepository.add(user);
    }

    public void logout() {
        currentUser = null;
    }

    private User useCurrentUser() {
        if (currentUser == null)
            throw new Error("Operation not permitted!");
        return currentUser;
    }
}
