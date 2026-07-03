#include "Service.h"
#include <algorithm>
#include <random>
using namespace std;

int Service::addActivity(int id,const string& title,const string& description,const string& type, int duration)
{
	Activity activity{ id,title,description,type,duration };
	Validator::validateActivity(activity);
	repository.store(activity);
	UndoActions.push_back(std::make_unique<UndoAdd>(activity,repository));
	return 1;
}

int Service::deleteActivity(int id) {
	if (id <= 0)
		throw ServiceException("Id-ul trebuie sa fie numar natural strict pozitiv!\n");
	const Activity deletedActivity = repository.find(id);
	repository.remove(id);
	UndoActions.push_back(std::make_unique<UndoDelete>(deletedActivity, repository));
	return 1;
}

int Service::updateActivity(int id,const string& title,const string& description,const string& type, int duration) {
	Activity newActivity{ id,title,description,type,duration };
	Validator::validateActivity(newActivity);
	const Activity oldActivity = repository.find(id);
	repository.update(newActivity);
	UndoActions.push_back(std::make_unique<UndoUpdate>(oldActivity,repository));
	return 1;
}

const Activity& Service::findActivity(int id) const{
	if(id<=0)
		throw ServiceException("Id-ul trebuie sa fie numar natural strict pozitiv!\n");
	return repository.find(id);
}

const vector<Activity>& Service::getAll() const{
	if (repository.getAll().size() == 0)
		throw ServiceException("Nu exista activitati!\n");
	return repository.getAll();
}

vector<Activity> Service::filterBy(const string& field, const string& value) const {
	vector<Activity> result;
	std::copy_if(repository.getAll().begin(), repository.getAll().end(), std::back_inserter(result),
		[&](const auto& activity) {
			return (field == "description" && activity.getDescription() == value) ||
				(field == "type" && activity.getType() == value);
		});
	if(result.size()==0)
		throw ServiceException("Nu exista activitati cu acest filtru!\n");
	return result;
}

vector<Activity> Service::sortBy(std::function<bool(const Activity&, const Activity&)> compare) const {
	auto activities = repository.getAll();

	sort(activities.begin(), activities.end(), [compare](const Activity& a1, const Activity& a2) { return compare(a1, a2); });

	return activities;
}

map<string, DTO> Service::createMap() {
	if (repository.getAll().size() == 0)
		throw ServiceException("Nu exista activitati!\n");
	raport.clear();
	for (const auto& activity : repository.getAll()) {
		string title = activity.getTitle();
		auto it = raport.find(title);
		if (it != raport.end())
			it->second.incrementFrequency();
		else
			raport.insert({ title,DTO{title} });
	}
	return raport;
}

void Service::undo() {
	if (UndoActions.empty())
		throw ServiceException("Nu se mai poate face undo!\n");
	UndoActions.back()->doUndo();
	UndoActions.pop_back();
}

