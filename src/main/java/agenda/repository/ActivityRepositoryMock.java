package agenda.repository;


import agenda.model.Activity;
import agenda.repository.interfaces.IActivityRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ActivityRepositoryMock implements IActivityRepository {
	List<Activity> activities = new LinkedList<>();

	@Override
	public List<Activity> getActivities() {
		return activities;
	}

	@Override
	public void addActivity(Activity activity) {
		if (get(activity.getId()) != null)
			throw new IllegalArgumentException("Activity all ready exists");

		for (Activity existingActivity : activities) {
			if (existingActivity.intersect(activity)) {
                throw new IllegalArgumentException("Activity overlaps with other activities!");
			}
		}

        activities.add(activity);
        save();
	}

	@Override
	public Activity get(Long id) {
	    return activities.stream().filter(x->x.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public Long getNewId() {
		return activities.stream().max(Comparator.comparing(Activity::getId)).map(Activity::getId).orElse(0L)+1L;
	}

	@Override
	public void save() {
	}
}
