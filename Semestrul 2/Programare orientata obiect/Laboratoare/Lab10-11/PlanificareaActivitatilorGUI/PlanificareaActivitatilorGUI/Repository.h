#pragma once
#include "Activity.h"
#include "Exception.h"
#include <vector>
#include <fstream>
using std::vector;

class RepositoryException :public Exception {
public:
	explicit RepositoryException(string message):Exception(message){}
};

class Repository {
private:
	vector<Activity>activities;

	bool exists(const Activity& activity);

public:
	Repository() = default;

	Repository(const Repository& other) = delete;

	/*
	* Adauga o activitate in lista de elemente
	* activity:referinta catre activitatea de adaugat
	* return:true daca adaugarea s-a realizat cu succes,false in caz contrar
	*/
	virtual bool store(const Activity& activity);

	/*
	* Sterge o activitate din lista dupa id
	* id:id-ul activitatii de sters(int)
	* return:true daca stergerea s-a realizat cuu succes,false altfel
	*/
	virtual bool remove(int id);

	/*
	* Actualizeaza o activitate din lista
	* newActivity:referinta catre activitatea noua
	* return:true daca activitatea s-a actualizat,false altfel
	*/
	virtual bool update(const Activity& newActivity);

	/*
	* Cauta o activitate dupa id
	* id:id-ul activitatii de cautat(int)
	* return:referinta activitatii gasite
	*/
	Activity& find(int id);

	/*
	* Returneaza lista de activitati din repository
	* return:referinta catre lista
	*/
	const vector<Activity>& getAll() const;

	virtual ~Repository();
};

class RepositoryFile :public Repository {
private:
	string fileName;
	void readFromFile();
	void writeToFile();
	void clearFile() {
		std::ofstream fin(fileName, std::ios::trunc);
		fin.close();
	}

public:
	RepositoryFile(string fileName) :Repository(), fileName{ fileName } {
		readFromFile();
	}

	bool store(const Activity& activity) override {
		Repository::store(activity);
		writeToFile();
		return true;
	}

	bool remove(int id) override {
		Repository::remove(id);
		writeToFile();
		return true;
	}

	bool update(const Activity& activity) override {
		Repository::update(activity);
		writeToFile();
		return true;
	}

	~RepositoryFile() override {

	}
};