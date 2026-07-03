#pragma once
#include "Repository.h"
#include "Validator.h"
#include "Exception.h"
#include "DTO.h"
#include "Undo.h"
#include <iostream>
#include <functional>
#include <map>


class ServiceException :public Exception {
public:
	explicit ServiceException(string message) :Exception(message) {}
};

class Service {
private:
	Repository& repository;
	std::map<string, DTO> raport;
	std::vector<std::unique_ptr<UndoAction>> UndoActions;
   
public:
	Service(Repository& repository) : repository{ repository } {}

	Service(const Service& other) = delete;
	/*
	* Functia de adaugare a unei activitati din service
	* id:id-ul activitatii
	* title:titlul activitatii
	* description:descrierea activitatii
	* type:tipul activitatii
	* duration:durata activitatii
	*/
	int addActivity(int id,const string& title, const string& description,const string& type, int duration);

	/*
	* Functia de stergere a unei activitati din service
	* id:id-ul activitatii de sters
	*/
	int deleteActivity(int id);

	/*
	* Functia de actualizare a unei activitati din service
	* id:id-ul activitatii
	* title:titlul noi activitatii
	* description:descrierea noi activitatii
	* type:tipul noi activitatii
	* duration:durata noi activitatii
	*/
	int updateActivity(int id,const string& title,const string& description,const string& type, int duration);

	/*
	* Functia de cautare a unei activitati din service
	* id:id-ul activitatii de cautat
	* return:referinta la activitatea cautata
	*/
	const Activity& findActivity(int id) const;
	
	/*
	* Functia returneaza lista de activitati din repository
	* return:referinta catre lista
	*/
	const vector<Activity>& getAll() const;

	/*
	* Functia de filtrare din service
	* field:referinta catre criteriul pentru care se face filtrarea
	* value:referinta catre numele valorii care se cauta
	* return:lista filtrata
	*/
	vector<Activity> filterBy(const string& field, const string& value) const;

	/*
	* Functia de sortare din service
	* field:referinta catre criteriul pentru care se face sortarea
	* return:lista sortata
	*/
	vector<Activity> sortBy(std::function<bool(const Activity&, const Activity&)> compare) const;

	std::map<string, DTO> createMap();

	void undo();
};