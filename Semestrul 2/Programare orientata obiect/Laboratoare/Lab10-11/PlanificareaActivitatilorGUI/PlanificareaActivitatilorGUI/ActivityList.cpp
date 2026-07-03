#include "ActivityList.h"
#include <random>
#include <fstream>
using namespace std;

void ActivityList::empty() {
	list.clear();
	notify();
}

void ActivityList::add(const string& title) {
	if (title == "")
		throw CartException("Titlul trebuie sa contina cel putin un caracter!\n");
	for (const auto& activity : activityService.getAll()) {
		if (title == activity.getTitle()) {
			list.push_back(activity);
			break;
		}
	}
	notify();
}

void ActivityList::exportCSV(const string& fileName) const {
	Validator::validateFile(fileName);
	ofstream fout(fileName);
	for(const auto& activity:list)
		fout << activity.getId() << "," << activity.getTitle() << "," << activity.getDescription() << "," << activity.getType() << "," << activity.getDuration() << endl;
	fout.close();
}

const vector<Activity>& ActivityList::getAll() const {
	return list;
}

void ActivityList::generateActivities(int number) {
	if (number <= 0)
		throw CartException("Numarul de activitati de generat trebuie sa fie numar natural strict pozitiv!");
	auto activities = activityService.getAll();
	default_random_engine generator(random_device{}());
	uniform_int_distribution<int> dist(0, (int)activities.size() - 1);
	int index = 0;
	while (index < number) {
		int randomIndex = dist(generator);
		list.push_back(activities[randomIndex]);
		index++;
	}
	notify();
}