package agenda.model;

import agenda.exceptions.InvalidFormatException;
import agenda.validators.ActivityValidator;

import java.util.*;
import java.util.stream.Collectors;

public class Activity {
	private Long id;
	private String name;
	private Date start;
	private Long duration;
	private String description;
	private List<Contact> contacts;

	public Activity(Long id, String name, Date start, Long duration, String description, List<Contact> contacts) {
		setId(id);
		setName(name);
		setStart(start);
		setDuration(duration);
		setDescription(description);
		setContacts(contacts);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (!ActivityValidator.isValidId(id))
			throw new InvalidFormatException("Cannot convert", "Invalid id");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!ActivityValidator.isValidName(name))
			throw new InvalidFormatException("Cannot convert", "Invalid name");
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		if (!ActivityValidator.isValidStart(start))
			throw new InvalidFormatException("Cannot convert", "Invalid start");
		this.start = start;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		if (!ActivityValidator.isValidDuration(duration))
			throw new InvalidFormatException("Cannot convert", "Invalid duration");
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		if (contacts == null)
			contacts = new ArrayList<>();
		this.contacts = contacts;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Activity))
			return false;
		Activity act = (Activity) obj;
		return act.id.equals(id);
	}

	public boolean intersect(Activity act) {
		return (start.getTime() < act.getStart().getTime() &&
						act.getStart().getTime() < start.getTime() + duration) ||
				(start.getTime() < act.getStart().getTime() + act.getDuration() &&
						act.getStart().getTime() + act.getDuration() < start.getTime() + duration);
	}

	public boolean hasSameDate(Date date) {
        Calendar thisCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        thisCalendar.setTime(start);
        otherCalendar.setTime(date);
        return thisCalendar.get(Calendar.DAY_OF_YEAR) == otherCalendar.get(Calendar.DAY_OF_YEAR) &&
                thisCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", start=" + start +
                ", duration=" + duration / 3600000 + ':' + duration % 3600000 / 60000 +
                ", description='" + description + '\'' +
                ", contacts=(\n\t"
				+ contacts.stream().map(Objects::toString).collect(Collectors.joining("\n\t")) + "\n)";
    }
}
