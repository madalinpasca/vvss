package agenda.repository;

import agenda.model.User;
import agenda.repository.interfaces.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryMock implements IUserRepository {
    List<User> userList = new ArrayList<>();

    public User get(String username) {
        return userList.stream().filter(x->x.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public void add(User user) {
        if (get(user.getUsername()) != null)
            throw new IllegalArgumentException("Username all ready exists!");
        userList.add(user);
        save();
    }

    @Override
    public void update(User user) {
        if (get(user.getUsername()) == null)
            throw new IllegalArgumentException("Username not found!");
        userList = userList.stream().map(x->x.equals(user)?user:x).collect(Collectors.toList());
        save();
    }

    @Override
    public void save() { }
}
