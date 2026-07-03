#include "ActivityList.h"
#include <random>
#include <fstream>
using namespace std;

void ActivityList::deleteList() {
	list.clear();
}

void ActivityList::add(const Activity& activity) {
	list.push_back(activity);
}

void ActivityList::exportListInCSV(const string& fileName) const {
	ofstream fout(fileName);
	for(const auto& activity:list)
		fout << activity.getId() << "," << activity.getTitle() << "," << activity.getDescription() << "," << activity.getType() << "," << activity.getDuration() << endl;
	fout.close();
}

const vector<Activity>& ActivityList::getAll() const {
	return list;
}