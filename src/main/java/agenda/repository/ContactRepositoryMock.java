package agenda.repository;

import agenda.model.Contact;
import agenda.repository.interfaces.IContactRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ContactRepositoryMock implements IContactRepository {
	List<Contact> contacts = new LinkedList<>();

	@Override
	public List<Contact> getContacts() {
		return contacts;
	}

	@Override
	public void addContact(Contact contact) {
		if (get(contact.getId()) != null)
			throw new IllegalArgumentException("Contact all ready exists!");
		contacts.add(contact);
		save();
	}

	@Override
	public void save() { }

	@Override
	public Long getNewId() {
		return contacts.stream().max(Comparator.comparing(Contact::getId)).map(Contact::getId).orElse(0L)+1L;
	}

	@Override
	public Contact get(Long id) {
		return contacts.stream().filter(x->x.getId().equals(id)).findFirst().orElse(null);
	}
}
