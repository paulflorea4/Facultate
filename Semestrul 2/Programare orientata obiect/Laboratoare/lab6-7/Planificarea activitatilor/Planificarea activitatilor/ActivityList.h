#pragma once

#include "Activity.h"
#include <vector>
using std::vector;

class ActivityList {
private:
	vector<Activity>list;
public:
	ActivityList() = default;

	//Functia de stergere a tuturor activitatilor din lista
	void deleteList();

	//Functia de adaugare a unei activitati in lista
	//activity:referinta catre activitatea de adaugat
	void add(const Activity& activity);

	//Functia care exporta lista intr-un fisier CSV
	//fileName:numele fisierului in care va fi exportata lista
	void exportListInCSV(const string& fileName) const;

	//Functia care returneaza lista de activitati
	//return>:referinta catre lista de activitati
	const vector<Activity>& getAll() const;
};