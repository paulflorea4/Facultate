#pragma once
#include <iostream>
#include <string>
#include <ostream>

using std::string;

class Activity {

private:
	int id=-1;
	string title;
	string description;
	string type;
	int duration=0;

public:

	Activity() = default;

	Activity(int id, string title, string description, string type, int duration) :id{ id }, title{ title }, description{ description }, type{ type }, duration{ duration } {}

	Activity(const Activity& other) :id{ other.id }, title{ other.title }, description{ other.description }, type{ other.type }, duration{ other.duration } {
		//std::cout << "S-a facut copiere" << std::endl;
	}

	Activity& operator=(const Activity& other) {
		if (this != &other)
		{
			id = other.id;
			title = other.title;
			description = other.description;
			type = other.type;
			duration = other.duration;
		}
		//std::cout << "S-a facut atribuire" << std::endl;
		return *this;
	}


	/*
	* Functia de obtinerea a id-ului
	* return:id-ul activitatii(int)
	*/
	int getId() const;

	/*
	* Functia de obtinerea a titlului
	* return:titlul activitatii(string)
	*/
	string getTitle() const;

	/*
	* Functia de obtinerea a descrierii
	* return:descrierea activitatii(string)
	*/
	string getDescription() const;

	/*
	* Functia de obtinerea a tipului
	* return:tipul activitatii(string)
	*/
	string getType() const;

	/*
	* Functia de obtinerea a id-ului
	* return:id-ul activitatii(int)
	*/
	int getDuration() const;

	/*
	* Functia de modificare a titlului
	*/
	void setTitle(const string newTitle);
	
	/*
	* Functia de modificare a descrierii
	*/
	void setDescription(const string newDescription);

	/*
	* Functia de modificare a tipului
	*/
	void setType(const string newType);

	/*
	* Functia de modificare a duratei
	*/
	void setDuration(const int newDuration);

	/*
	* Functia de suprascriere a operatiei de afisare a unei activitati
	* out:referinta catre fluxul de iesire
	* activity:referinta catre activitatea de afisat
	* return:referinta catre fluxul de iesire
	*/
	friend std::ostream& operator<<(std::ostream& out, const Activity& activity) {
		out<< "Activitatea: " + std::to_string(activity.getId()) +
			" | Titlu: " + activity.getTitle() +
			" | Descriere: " + activity.getDescription() +
			" | Tip: " + activity.getType() +
			" | Durata: " + std::to_string(activity.getDuration());
		return out;
	}

	bool static compareByTitle(const Activity& a, const Activity& b);

	bool static compareByDescription(const Activity& a, const Activity& b);

	bool static compareByTypeAndDuration(const Activity& a, const Activity& b);

	~Activity(){}
};