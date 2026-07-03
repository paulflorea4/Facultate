#pragma once
#include "Activity.h"
#include "Exception.h"
#include <vector>
#include <fstream>
#include <random>
using std::vector;

class RepositoryException :public Exception {
public:
	explicit RepositoryException(string message):Exception(message){}
};

class RepositoryRandomException : public RepositoryException {
public:
	explicit RepositoryRandomException(string message) : RepositoryException(message) {}
};

class RepositoryAbstract {
public:
	virtual bool store(const Activity& activity) = 0;
	virtual bool remove(int id) = 0;
	virtual bool update(const Activity& newActivity) = 0;
	virtual Activity& find(int id) = 0;
	virtual const vector<Activity>& getAll() const = 0;

	virtual ~RepositoryAbstract() = default;
};


class RepositoryRandom : public RepositoryAbstract {
private:
	vector<Activity> activities;
	mutable std::default_random_engine engine{ std::random_device{}() };
	mutable std::uniform_real_distribution<double> dist{ 0.0, 2.0 };

	void maybeThrow() const {
		double random = dist(engine);
		if (random > 0.0 && random < 1.0) {
			throw RepositoryRandomException("Random failure");
		}
	}

public:
	bool store(const Activity& activity) override {
		maybeThrow();
		activities.push_back(activity);
		return true;
	}

	bool remove(int id) override {
		maybeThrow();
		auto it = std::remove_if(activities.begin(), activities.end(),
			[id](const Activity& a) { return a.getId() == id; });
		if (it == activities.end()) return false;
		activities.erase(it, activities.end());
		return true;
	}

	bool update(const Activity& newActivity) override {
		maybeThrow();
		for (auto& activity : activities) {
			if (activity.getId() == newActivity.getId()) {
				activity = newActivity;
				return true;
			}
		}
		return false;
	}

	Activity& find(int id) override {
		maybeThrow();
		for (auto& activity : activities) {
			if (activity.getId() == id) return activity;
		}
		throw RepositoryException("Activity not found");
	}

	const vector<Activity>& getAll() const override {
		maybeThrow();
		return activities;
	}
};


class Repository:public RepositoryAbstract {
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
	bool store(const Activity& activity) override;

	/*
	* Sterge o activitate din lista dupa id
	* id:id-ul activitatii de sters(int)
	* return:true daca stergerea s-a realizat cuu succes,false altfel
	*/
	 bool remove(int id) override;

	/*
	* Actualizeaza o activitate din lista
	* newActivity:referinta catre activitatea noua
	* return:true daca activitatea s-a actualizat,false altfel
	*/
	bool update(const Activity& newActivity) override;

	/*
	* Cauta o activitate dupa id
	* id:id-ul activitatii de cautat(int)
	* return:referinta activitatii gasite
	*/
	Activity& find(int id) override;

	/*
	* Returneaza lista de activitati din repository
	* return:referinta catre lista
	*/
	const vector<Activity>& getAll() const override;

	~Repository() override {};
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