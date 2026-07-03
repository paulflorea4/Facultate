#pragma once
#include "Repository.h"
#include "Observer.h"
#include <exception>
using namespace std;

class Service :public Observable {
private:
	Repository& repository;
public:
	Service(Repository& repository) :repository{ repository } {}
	//Functia de adaugare a unei parcari din service
	//adresa-adresa parcarii
	//linii-numarul de linii
	//coloane-numarul de coloane
	//stare-starea parcarii
	void add(string adresa, int linii, int coloane, string stare);

	//Functia de modificare a unei parcari din service
	//adresa-adresa parcarii
	//linii-numarul de linii
	//coloane-numarul de coloane
	//stare-starea parcarii
	//return 1: daca numarul de linii e invalid
	//return 2: daca numarul de coloane e invalid
	//return 3: daca starea nu are dimensiunea egala cu linii*coloane
	//return 4: daca starea nu contine numai X si -
	//return 0:daca modificarea s-a facut cu succes
	int update(string adresa, int linii, int coloane, string stare);

	//Functia returneaza lista de parcari din repository
	vector<Parcare>& getAll() {
		return repository.getAll();
	}
};