package agenda.repository;

import agenda.exceptions.InvalidFormatException;
import agenda.model.Activity;
import agenda.model.Contact;
import agenda.model.User;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;
import agenda.repository.interfaces.IUserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepositoryFile extends UserRepositoryMock implements IUserRepository {

	private static final String filename = "files/users.txt";
	
	public UserRepositoryFile(IActivityRepository activityRepository, IContactRepository contactRepository) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
			String line;
			while ((line = br.readLine()) != null) {
				User u = fromString(line, activityRepository, contactRepository);
				super.add(u);
			}
		} catch (IOException e) {
			throw new InvalidFormatException("File error " + filename, e.getMessage());
		}
	}

	public static String toString(User user) {
		return user.getName() + "#" + user.getUsername() + "#" + user.getPassword() + "#" +
				user.
						getActivities().
						stream().
						map(Activity::getId).
						map(Objects::toString).
						reduce((x, y)->x+","+y).
						orElse("")
				+ "#" +
				user.
						getContacts().
						stream().
						map(Contact::getId).
						map(Objects::toString).
						reduce((x, y)->x+","+y).
						orElse("");
	}

	@Override
	public void save() {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename))) {
			for (User u : userList)
				pw.println(toString(u));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static User fromString(String line,
								  IActivityRepository activityRepository,
								  IContactRepository contactRepository)
	{
		String[] split = line.split("#");
		if (split.length < 3 || split.length > 5) {
			throw new InvalidFormatException("Cannot convert", "Invalid data");
		}

		List<Activity> activities = new ArrayList<>();
		if (split.length > 3 && !split[3].equals("")) {
			for (String activityId : split[3].split(",")) {
				try {
					Activity activity = activityRepository.get(Long.valueOf(activityId));
					if (activity != null)
						activities.add(activity);
				} catch (NumberFormatException ignored) {
				}
			}
		}

		List<Contact> contacts = new ArrayList<>();
		if (split.length > 4 && !split[4].equals("")) {
			for (String contactId : split[4].split(",")) {
				try {
					Contact contact = contactRepository.get(Long.valueOf(contactId));
					if (contact != null)
						contacts.add(contact);
				} catch (NumberFormatException ignored) {
				}
			}
		}

		return new User(split[0], split[1], split[2], activities, contacts);
	}
}
