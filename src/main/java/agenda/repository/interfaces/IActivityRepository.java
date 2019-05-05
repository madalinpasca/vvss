package agenda.repository.interfaces;

import agenda.model.Activity;

import java.util.List;

public interface IActivityRepository {
	List<Activity> getActivities();
	void addActivity(Activity activity);
    Activity get(Long valueOf);
	Long getNewId();
	void save();
}
