#pragma once

#include "Activity.h"
#include <vector>
#include <Observable.h>
#include "Service.h"
using std::vector;

class CartException :public Exception {
public:
	explicit CartException(string message) :Exception(message) {}
};

class ActivityList :public Observable{
private:
	vector<Activity>list;
	Service& activityService;
public:
	ActivityList(Service& service) :activityService{ service } {};

	//Functia de stergere a tuturor activitatilor din lista
	void empty();

	//Functia de adaugare a unei activitati in lista
	//activity:referinta catre activitatea de adaugat
	void add(const string& title);

	//Functia care exporta lista intr-un fisier CSV
	//fileName:numele fisierului in care va fi exportata lista
	void exportCSV(const string& fileName) const;

	//Functia care returneaza lista de activitati
	//return>:referinta catre lista de activitati
	const vector<Activity>& getAll() const;

	//Functia care genereaza activitati in lista
	//number: numarul de activitati de generat
	//activities:lista de activitati din care se genereaza activitatile
	void generateActivities(int number);
};