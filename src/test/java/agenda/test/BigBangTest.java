package agenda.test;

import agenda.exceptions.InvalidFormatException;
import agenda.model.Activity;
import agenda.model.Contact;
import agenda.model.User;
import agenda.repository.*;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;
import agenda.repository.interfaces.IUserRepository;
import agenda.service.Service;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BigBangTest {
    @Test
    public void testA() {
        IContactRepository contactRepository = new ContactRepositoryMock();
        IActivityRepository activityRepository = new ActivityRepositoryMock();
        IUserRepository userRepository = new UserRepositoryMock();
        userRepository.add(new User("test",
                "test",
                "test",
                null,
                null));
        Service service = new Service(userRepository, activityRepository, contactRepository);
        service.login("test", "test");

        try {
            service.addContact(null, "test", "07", "test");
            Assert.fail();
        } catch (NullPointerException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testB() throws NoSuchMethodException, IllegalAccessException {
        IContactRepository contactRepository = new ContactRepositoryMock();
        Method method = ActivityRepositoryFile.class.getDeclaredMethod("fromString",
                String.class,
                IContactRepository.class);
        method.setAccessible(true);

        try {
            method.invoke(null,"x#y", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void testC() {
        Service service = new Service(new UserRepositoryMock(),
                new ActivityRepositoryMock(),
                new ContactRepositoryMock());

        try {
            service.getActivities(new Date());
            Assert.fail();
        } catch (Error ignored) { }
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testPABC() throws IOException {
        Path contactsFilePath = Paths.get("files/contacts.txt");
        Path activitiesFilePath = Paths.get("files/activities.txt");
        Path usersFilePath = Paths.get("files/users.txt");
        Path contactsBackupFilePath = Paths.get("files/contacts.txt.bkup");
        Path activitiesBackupFilePath = Paths.get("files/activities.txt.bkup");
        Path usersBackupFilePath = Paths.get("files/users.txt.bkup");
        saveFile(activitiesFilePath, activitiesBackupFilePath);
        saveFile(contactsFilePath, contactsBackupFilePath);
        saveFile(usersFilePath, usersBackupFilePath);

        IContactRepository contactRepository = new ContactRepositoryFile();
        IActivityRepository activityRepository = new ActivityRepositoryFile(contactRepository);
        IUserRepository userRepository = new UserRepositoryFile(activityRepository, contactRepository);
        Service service = new Service(userRepository, activityRepository, contactRepository);

        service.register("ggg", "ggg", "ggg");
        boolean logged = service.login("ggg", "ggg");
        Assert.assertTrue(logged);

        service.addContact("ggg", "ggg",  "07", "ggg");
        List<Contact> contacts = service.getContacts();
        Assert.assertEquals(1, contacts.size());
        Assert.assertEquals("ggg", contacts.get(0).getName());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.APRIL, 12, 12,0);
        service.addActivity("ggg",calendar.getTime(), 3600000L,"ggg", contacts);
        List<Activity> activities = service.getActivities(calendar.getTime());

        Assert.assertEquals(1, activities.size());
        Assert.assertEquals("ggg", activities.get(0).getName());
        Assert.assertEquals(1, activities.get(0).getContacts().size());
        Assert.assertEquals("ggg", activities.get(0).getContacts().get(0).getName());

        reloadFile(activitiesBackupFilePath, activitiesFilePath);
        reloadFile(contactsBackupFilePath, contactsFilePath);
        reloadFile(usersBackupFilePath, usersFilePath);
    }

    private void reloadFile(Path activitiesBackupFilePath, Path activitiesFilePath) throws IOException {
        if (Files.exists(activitiesFilePath)) {
            Files.delete(activitiesFilePath);
        }
        if (Files.exists(activitiesBackupFilePath)) {
            Files.move(activitiesBackupFilePath, activitiesFilePath);
        } else {
            Files.createFile(activitiesFilePath);
        }
    }

    private void saveFile(Path activitiesFilePath, Path activitiesBackupFilePath) throws IOException {
        if (Files.exists(activitiesFilePath)) {
            Files.move(activitiesFilePath, activitiesBackupFilePath);
        }
        Files.createFile(activitiesFilePath);
    }
}
