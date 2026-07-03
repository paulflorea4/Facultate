#pragma once
#include "Parcare.h"
#include <vector>
using std::vector;

class Repository {
private:
	string fisier;
	vector<Parcare> parcari;
	//Functia de salvare a datelor in fisier
	void save();
	//Functia de incarcare a datelor din fisier
	void load();
public:
	Repository(string fisier) :fisier{ fisier } {
		load();
	}
	//Functia de adaugare a unei parcari in lista
	//p-referinta catre parcarea de adaugat
	void add(const Parcare& p);
	//Functia de modificare a unei parcari in lista
	//p-referinta catre parcarea de modificat
	void update(const Parcare& p);
	//Functia returneaza lista de parcari sortate dupa numarul de locuri
	vector<Parcare>& getAll();
};