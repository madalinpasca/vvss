package agenda.repository;

import agenda.exceptions.InvalidFormatException;
import agenda.model.Contact;
import agenda.repository.interfaces.IContactRepository;

import java.io.*;

public class ContactRepositoryFile extends ContactRepositoryMock implements IContactRepository {

	private static final String filename = "files/contacts.txt";

	public ContactRepositoryFile() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
			String line;
			while ((line = br.readLine()) != null) {
				Contact c = fromString(line);
				contacts.add(c);
			}
		} catch (IOException e) {
			throw new InvalidFormatException("File error " + filename, e.getMessage());
		}
	}

	@Override
	public void save() {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename))) {
			for (Contact c : contacts)
				pw.println(toString(c));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String toString(Contact contact) {
		return contact.getId() + "#" + contact.getName() + "#" + contact.getAddress() + "#" + contact.getPhone() +
				"#" + contact.getEmail();
	}

	private static Contact fromString(String line) {
		String[] split = line.split("#");
		if (split.length != 5) {
			throw new InvalidFormatException("Cannot convert", "Invalid data length");
		}

		Long id;

		try {
			id = Long.valueOf(split[0]);
		} catch (NumberFormatException ex) {
			throw new InvalidFormatException("Cannot convert", "Invalid id");
		}

		return new Contact(id, split[1], split[2], split[3], split[4]);
	}
}
