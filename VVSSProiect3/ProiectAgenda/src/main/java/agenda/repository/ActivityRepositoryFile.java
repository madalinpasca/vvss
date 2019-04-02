package agenda.repository;

import agenda.exceptions.InvalidFormatException;
import agenda.model.Activity;
import agenda.model.Contact;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ActivityRepositoryFile extends ActivityRepositoryMock implements IActivityRepository {

	private static final String filename = "files/activities.txt";

	public ActivityRepositoryFile(IContactRepository contactRepository) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
			String line;
			while ((line = br.readLine()) != null) {
				Activity act = fromString(line, contactRepository);
				activities.add(act);
			}
		} catch (IOException e) {
			throw new InvalidFormatException("File error " + filename, e.getMessage());
		}
	}

	@Override
	public void save() {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename))) {
			for (Activity a : activities)
				pw.println(toString(a));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private static String toString(Activity activity) {
        return activity.getId() + "#" + activity.getName() + "#" + activity.getStart().getTime() + "#" +
                activity.getDuration() + "#" + activity.getDescription() + "#" + activity.getContacts().stream()
                    .map(Contact::getId).map(Objects::toString).reduce((x, y)->x+","+y).orElse("");
    }

	private static Activity fromString(String line, IContactRepository contactRepository) {
		String[] split = line.split("#");
		if (split.length < 4 || split.length > 6) {
			throw new InvalidFormatException("Cannot convert", "Invalid data");
		}

		Long id;
		try {
			id = Long.valueOf(split[0]);
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Cannot convert", "Invalid id");
		}

		Date start;
		try {
			start = new Date(Long.valueOf(split[2]));
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Cannot convert", "Invalid start");
		}

		Long duration;
		try {
			duration = Long.valueOf(split[3]);
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Cannot convert", "Invalid duration");
		}

		List<Contact> contacts = new ArrayList<>();
		if (split.length == 6) {
			for (String contactId : split[5].split(",")) {
				try {
					Contact contact = contactRepository.get(Long.valueOf(contactId));
					if (contact != null)
						contacts.add(contact);
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return new Activity(id, split[1], start, duration, split.length > 4 ? split[4] : "", contacts);
	}
}
