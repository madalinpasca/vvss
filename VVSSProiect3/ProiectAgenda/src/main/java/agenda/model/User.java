package agenda.model;

import agenda.exceptions.InvalidFormatException;
import agenda.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private String name;
	private List<Activity> activities;
	private List<Contact> contacts;

	public User(String name,
				String username,
				String password,
				List<Activity> activities,
				List<Contact> contacts) {
		setName(name);
		setUsername(username);
		setPassword(password);
		setActivities(activities);
		setContacts(contacts);
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
        if (!UserValidator.isValidName(name))
            throw new InvalidFormatException("Cannot convert", "Invalid name");
        this.name = name;
    }

	public String getUsername() {
	    return username;
	}

	public void setUsername(String username) {
		if (!UserValidator.isValidUsername(username))
			throw new InvalidFormatException("Cannot convert", "Invalid username");
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		if (!UserValidator.isValidPassword(password))
			throw new InvalidFormatException("Cannot convert", "Invalid password");
		this.password = password;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		if (activities == null) {
			activities = new ArrayList<>();
		}
		this.activities = activities;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		if (contacts == null) {
			contacts = new ArrayList<>();
		}
		this.contacts = contacts;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User)) return false;
		User u = (User)obj;
		return u.username.equals(username);
	}

	@Override
	public String toString() {
		return name;
	}
}
