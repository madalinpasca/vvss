package agenda.test;

import agenda.exceptions.InvalidFormatException;
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

public class AddContactTest {
	private Service service;
	private final static String validAddress = "test";
	private final static String validPhone = "07";
	private final static String validName = "test";
	private final static String validEmail = "test";

	@Before
	public void setUp() {
        IContactRepository contactRepository = new ContactRepositoryMock();
        IActivityRepository activityRepository = new ActivityRepositoryMock();
        IUserRepository userRepository = new UserRepositoryMock();
        userRepository.add(new User("test",
                "test",
                "test",
                null,
                null));
        service = new Service(userRepository, activityRepository, contactRepository);
	    service.login("test", "test");
	}

	@Test
	public void testCase1() {
	    try {
            service.addContact(null, validAddress, validPhone, validEmail);
            Assert.fail();
	    } catch (NullPointerException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
	}

    @Test
    public void testCase2() {
	    service.addContact("test", validAddress, validPhone, validEmail);
        Assert.assertEquals("test", service.getContacts().get(0).getName());
	}

    @Test
    public void testCase3() {
        try {
            service.addContact("test test test", validAddress, validPhone, validEmail);
            Assert.fail();
        } catch (InvalidFormatException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase4() {
        try {
            service.addContact(validName, validAddress, null, validEmail);
            Assert.fail();
        } catch (NullPointerException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase5() {
        service.addContact(validName, validAddress, "+40 7", validEmail);
        Assert.assertEquals("+40 7", service.getContacts().get(0).getPhone());
    }

    @Test
    public void testCase6() {
        service.addContact(validName, validAddress, "07", validEmail);
        Assert.assertEquals("07", service.getContacts().get(0).getPhone());
    }

    @Test
    public void testCase7() {
        try {
            service.addContact(validName, validAddress, "07 6", validEmail);
            Assert.fail();
        } catch (InvalidFormatException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase8() {
        try {
            service.addContact(validName, validAddress, "7", validEmail);
            Assert.fail();
        } catch (InvalidFormatException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase9() {
        try {
            service.addContact(validName, validAddress, "+40", validEmail);
            Assert.fail();
        } catch (InvalidFormatException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase10() {
        try {
            service.addContact(validName, validAddress, "+40 74 56", validEmail);
            Assert.fail();
        } catch (InvalidFormatException ignored) {
            Assert.assertEquals(0, service.getContacts().size());
        }
    }

    @Test
    public void testCase11() {
        service.addContact("test test", validAddress, validPhone, validEmail);
        Assert.assertEquals("test test", service.getContacts().get(0).getName());
    }

}
