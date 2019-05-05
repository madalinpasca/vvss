package agenda.repository.interfaces;

import java.util.List;

import agenda.model.Contact;

public interface IContactRepository {
	List<Contact> getContacts();
	void addContact(Contact contact);
    Contact get(Long id);
	Long getNewId();
	void save();
}
