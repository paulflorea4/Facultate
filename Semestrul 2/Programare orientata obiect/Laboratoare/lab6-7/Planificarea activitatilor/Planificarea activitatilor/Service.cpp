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
	if (id < 0)
		throw ServiceException("Id-ul trebuie sa fie numar pozitiv!\n");
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
	if(id<0)
		throw ServiceException("Id-ul trebuie sa fie numar pozitiv!\n");
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

void Service::emptyActivityList() {
	list.deleteList();
}

void Service::addActivityInList(const string& title) {
	if (title.size() == 0)
		throw ServiceException("Titlul este invalid!");
	auto it = find_if(repository.getAll().begin(), repository.getAll().end(), [&title](const Activity& activity) { return activity.getTitle() == title; });
	if (it == repository.getAll().end())
		throw ServiceException("Nu exista activitate cu titlul dat!");
	list.add(*it);
}

void Service::exportListCSV(const string& fileName) const {
	if (list.getAll().size() == 0)
		throw ServiceException("Nu exista activitati in lista!\n");
	list.exportListInCSV(fileName);
}

const vector<Activity>& Service::getActivitiesFromList() const {
	if (list.getAll().size() == 0)
		throw ServiceException("Nu exista activitati in lista!\n");
	return list.getAll();
}

void Service::generateActivities(int number) {
	if (number <= 0)
		throw ServiceException("Numarul de activitati de generat trebuie sa fie strict pozitiv!\n");

	vector<string> titles = {
	"Desen", "Pictura", "Invatare", "Citit", "Film",
	"Sport", "Curs", "Joaca", "Scris", "Canta"
	};

	vector<string> descriptions = {
		"Creativitate", "Imaginatie", "Programare", "Stiinta", "Actiune",
		"Fitness", "Design", "Distractie", "Fictiune", "Muzica"
	};

	vector<string> types = {
		"Hobby", "Educatie", "Timp",
		"Sanatate", "Relaxare", "Creatie", "Arta"
	};

	default_random_engine generator(random_device{}());
	uniform_int_distribution<int> distTitle(0, (int)titles.size() - 1);
	uniform_int_distribution<int> distDescription(0, (int)descriptions.size() - 1);
	uniform_int_distribution<int> distType(0, (int)types.size() - 1);
	uniform_int_distribution<int> distDuration(1, 10);
	uniform_int_distribution<int> distId(1, 100);

	int index = 0;
	while(index<number){
		int id = distId(generator);
		string title = titles[distTitle(generator)];
		string description = descriptions[distDescription(generator)];
		string type = types[distType(generator)];
		int duration = distDuration(generator);

		try {
			repository.store(Activity(id, title, description, type, duration));
			index++;
		}
		catch (RepositoryException&) {
			continue; }
	}
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

