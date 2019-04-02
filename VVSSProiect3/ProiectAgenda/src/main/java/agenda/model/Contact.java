package agenda.model;

import agenda.exceptions.InvalidFormatException;
import agenda.validators.ContactValidator;

public class Contact {
	private Long id;
	private String name;
	private String address;
	private String phone;
	private String email;

	public Contact(Long id, String name, String address, String phone, String email) {
		setId(id);
		setName(name);
		setAddress(address);
		setPhone(phone);
		setEmail(email);
	}

	private void setEmail(String email) {
		if (!ContactValidator.isValidEmail(email))
			throw new InvalidFormatException("Cannot convert", "Invalid email");
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		if (!ContactValidator.isValidId(id))
			throw new InvalidFormatException("Cannot convert", "Invalid id");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!ContactValidator.isValidName(name))
			throw new InvalidFormatException("Cannot convert", "Invalid name");
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	private void setAddress(String address) {
		if (!ContactValidator.isValidAddress(address))
			throw new InvalidFormatException("Cannot convert", "Invalid address");
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	private void setPhone(String phone) {
		if (!ContactValidator.isValidPhone(phone))
			throw new InvalidFormatException("Cannot convert", "Invalid phone number");
		this.phone = phone;
	}

    @Override
    public String toString() {
        return "name='" + name + '\'' +
				"address='" + address + '\'' +
                "phone=" + phone +
				"email='" + email + '\'';
    }

    @Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Contact))
			return false;
		Contact o = (Contact)obj;
		return o.id.equals(id);
	}

	public String getEmail() {
		return email;
	}
}
