#include "Repository.h"
#include <sstream>
using namespace std;

bool Repository::store(const Activity& activity)
{
	if (exists(activity)) {
		throw RepositoryException("Exista deja o activitate cu id-ul " + to_string(activity.getId())+ "!\n");
	}
	activities.push_back(activity);
	return true;
}

Activity& Repository::find(int id) {
	auto it = find_if(activities.begin(), activities.end(),[&id](const Activity& activity) { return activity.getId() == id; });
	if (it != activities.end())
		return *it;
	throw RepositoryException("Nu exista activitate cu id-ul " + to_string(id) + "!\n"); }

bool Repository::remove(int id) {
	auto it = find_if(activities.begin(), activities.end(),[&id](const Activity& activity) { return activity.getId() == id; });
	if (it != activities.end()) {
		activities.erase(it);
		return true;
	}
	throw RepositoryException("Nu exista activitate cu id-ul " + to_string(id) + "!\n"); }


bool Repository::update(const Activity& newActivity) {
	if (!exists(newActivity))
		throw RepositoryException("Nu exista activitate cu id-ul " + to_string(newActivity.getId()) + "!\n");
	Activity& activity = find(newActivity.getId());
	activity = newActivity;
	return true;
}

bool Repository::exists(const Activity& activity){
	try {
		find(activity.getId());
		return true;
	}
	catch (RepositoryException){
		return false;}
}

const vector<Activity>& Repository::getAll() const{
	return activities;
}

void RepositoryFile::readFromFile() {
	ifstream fin(fileName);
	if (!fin.is_open()) {
		throw RepositoryException("Fisierul " + fileName + " nu a putut fi deschis!\n");
	}
	while (true) {
		string line;
		getline(fin >> ws, line);

		if (line == "")
			break;

		istringstream ss(line);
		string idStr, title, description, type, durationStr;

		getline(ss, idStr, ',');
		getline(ss, title, ',');
		getline(ss, description, ',');
		getline(ss, type, ',');
		getline(ss, durationStr, ',');

		int id = stoi(idStr);
		int duration = stoi(durationStr);

		Repository::store(Activity(id, title, description, type, duration));
	}
	fin.close();
}

void RepositoryFile::writeToFile() {
	ofstream fout(fileName);
	if (!fout.is_open()) {
		throw RepositoryException("Fisierul " + fileName + " nu a putut fi deschis!\n");
	}
	for (auto& activity : Repository::getAll()) {
		fout << activity.getId() << "," << activity.getTitle() << "," << activity.getDescription() << "," << activity.getType() << "," << activity.getDuration() << endl;
	}
	fout.close();
}
