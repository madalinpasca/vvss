package agenda.test;

import agenda.exceptions.InvalidFormatException;
import agenda.model.Activity;
import agenda.model.Contact;
import agenda.repository.ActivityRepositoryFile;
import agenda.repository.ContactRepositoryMock;
import agenda.repository.interfaces.IContactRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AddActivityTest {
    private IContactRepository contactRepository;
    private Method method;

    @Before
    public void setUp() throws NoSuchMethodException {
        contactRepository = new ContactRepositoryMock();
        method = ActivityRepositoryFile.class.getDeclaredMethod("fromString",
                String.class,
                IContactRepository.class);
        method.setAccessible(true);
    }

    @Test
    public void test1() throws IllegalAccessException {
        try {
            method.invoke(null,"x#y", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void test2() throws IllegalAccessException {
        try {
            method.invoke(null,"a#bcd#c#d#e#f#g", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void test3() throws IllegalAccessException {
        try {
            method.invoke(null,"a#bcd#c#d#e#f", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void test4() throws IllegalAccessException {
        try {
            method.invoke(null,"15#bcd#c#d#e#f", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void test5() throws IllegalAccessException {
        try {
            method.invoke(null,"15#bcd#1557487800000#d#e#f", contactRepository);
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof InvalidFormatException);
        }
    }

    @Test
    public void test6() throws IllegalAccessException, InvocationTargetException {
        Activity activity = (Activity) method.invoke(null,"15#bcd#1557487800000#5400000", contactRepository);
        Assert.assertEquals(15L, (long)activity.getId());
        Assert.assertEquals("bcd", activity.getName());
        Assert.assertEquals(1557487800000L, activity.getStart().getTime());
        Assert.assertEquals(5400000L, (long)activity.getDuration());
        Assert.assertEquals("", activity.getDescription());
        Assert.assertEquals(0, activity.getContacts().size());
    }

    @Test
    public void test7() throws IllegalAccessException, InvocationTargetException {
        Activity activity = (Activity) method.invoke(null,"15#bcd#1557487800000#5400000#d", contactRepository);
        Assert.assertEquals(15L, (long)activity.getId());
        Assert.assertEquals("bcd", activity.getName());
        Assert.assertEquals(1557487800000L, activity.getStart().getTime());
        Assert.assertEquals(5400000L, (long)activity.getDuration());
        Assert.assertEquals("d", activity.getDescription());
        Assert.assertEquals(0, activity.getContacts().size());
    }

    @Test
    public void test8() throws IllegalAccessException, InvocationTargetException {
        Activity activity = (Activity) method.invoke(null,"15#bcd#1557487800000#5400000#d#e", contactRepository);
        Assert.assertEquals(15L, (long)activity.getId());
        Assert.assertEquals("bcd", activity.getName());
        Assert.assertEquals(1557487800000L, activity.getStart().getTime());
        Assert.assertEquals(5400000L, (long)activity.getDuration());
        Assert.assertEquals("d", activity.getDescription());
        Assert.assertEquals(0, activity.getContacts().size());
    }

    @Test
    public void test9() throws IllegalAccessException, InvocationTargetException {
        Contact contact = new Contact(1L, "ggg", "ggg", "07", "ggg");
        contactRepository.addContact(contact);

        Activity activity = (Activity) method.invoke(null,"15#bcd#1557487800000#5400000#d#1,2", contactRepository);
        Assert.assertEquals(15L, (long)activity.getId());
        Assert.assertEquals("bcd", activity.getName());
        Assert.assertEquals(1557487800000L, activity.getStart().getTime());
        Assert.assertEquals(5400000L, (long)activity.getDuration());
        Assert.assertEquals("d", activity.getDescription());
        Assert.assertEquals(1, activity.getContacts().size());
        Assert.assertEquals(1L, (long)activity.getContacts().get(0).getId());
    }
}
