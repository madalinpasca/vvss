package agenda.test;

import agenda.model.Activity;
import agenda.model.User;
import agenda.repository.ActivityRepositoryMock;
import agenda.repository.ContactRepositoryMock;
import agenda.repository.UserRepositoryMock;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;
import agenda.repository.interfaces.IUserRepository;
import agenda.service.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetActivitiesTest {
    private IUserRepository userRepository;
    private IContactRepository contactRepository;
    private IActivityRepository activityRepository;
    private Service service;

    @Before
    public void setup() {
        userRepository = new UserRepositoryMock();
        contactRepository = new ContactRepositoryMock();
        activityRepository = new ActivityRepositoryMock();
        service = new Service(userRepository, activityRepository, contactRepository);
    }

    @Test
    public void test1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.APRIL, 12, 12,0);
        activityRepository.addActivity(new Activity(1L,
                "ggg",
                calendar.getTime(),
                3600000L,
                "ggg",
                new ArrayList<>()));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2019, Calendar.APRIL, 12, 10,0);
        activityRepository.addActivity(new Activity(2L,
                "ggg",
                calendar2.getTime(),
                3600000L,
                "ggg",
                new ArrayList<>()));
        userRepository.add(new User("ggg",
                "ggg",
                "ggg",
                activityRepository.getActivities(),
                new ArrayList<>()));
        service.login("ggg","ggg");
        List<Activity> activities = service.getActivities(calendar2.getTime());
        Assert.assertEquals(2, activities.size());
        Assert.assertEquals((Long) 2L, activities.get(0).getId());
        Assert.assertEquals((Long) 1L, activities.get(1).getId());
    }

    @Test
    public void test2() {
        try {
            service.getActivities(new Date());
            Assert.fail();
        } catch (Error ignored) { }
    }
}
