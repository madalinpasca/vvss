package agenda.repository.interfaces;

import agenda.model.User;

public interface IUserRepository {
	User get(String username);
	void add(User user);
	void update(User user);
	void save();
}
