package agenda;

import agenda.UI.Console;
import agenda.repository.ActivityRepositoryFile;
import agenda.repository.ContactRepositoryFile;
import agenda.repository.UserRepositoryFile;
import agenda.repository.interfaces.IActivityRepository;
import agenda.repository.interfaces.IContactRepository;
import agenda.repository.interfaces.IUserRepository;
import agenda.service.Service;

public class Main {
	public static void main(String[] args) {
		IContactRepository contactRepository = new ContactRepositoryFile();
		IActivityRepository activityRepository = new ActivityRepositoryFile(contactRepository);
		IUserRepository userRepository = new UserRepositoryFile(activityRepository, contactRepository);
		Service service = new Service(userRepository, activityRepository, contactRepository);
		Console ui = new Console(service);
		ui.show();
	}
}