#include <iostream>
#include <string>
#include "UI.h"
#include <stdexcept>
#include <regex>

using namespace std;

void UI::printMenuUI() const{
	cout << "~Planificarea activitatilor~\n";
	cout << "1.Adauga activitate\n";
	cout << "2.Sterge activitate\n";
	cout << "3.Modifica activitate\n";
	cout << "4.Cauta activitate\n";
	cout << "5.Filtrare activitati\n";
	cout << "6.Sortare activitati\n";
	cout << "7.Afiseaza activitati\n";
	cout << "8.Gestioneaza lista de activitati\n";
	cout << "9.Afiseaza raport\n";
	cout << "U.Undo\n";
	cout << "E.Iesire aplicatie\n";
}

void UI::addActivityUI() {
	string idStr, durationStr, title, description, type;
	int id, duration;

	cout << "Introduceti id:";
	getline(cin, idStr);

	try {
		id = stoi(idStr);
	}
	catch (invalid_argument&) {
		cout << "Id-ul trebuie sa fie un numar natural!\n\n";
		return;
	}

	cout << "Introduceti titlu:";
	getline(cin, title);

	cout << "Introduceti descriere:";
	getline(cin, description);

	cout << "Introduceti tipul:";
	getline(cin, type);

	cout << "Introduceti durata:";
	getline(cin, durationStr);
	try {
		duration = stoi(durationStr);
	}
	catch (invalid_argument&) {
		cout << "Durata trebuie sa fie un numar natural!\n\n";
		return;
	}

	try {
		activityService.addActivity(id, title, description, type, duration);
		cout << "Activitate adaugata cu succes!\n";
	}
	catch (Exception& errorMessage)
	{
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::deleteActivityUI() {
	int id;
	string idStr;
	cout << "Introduceti id:";
	getline(cin, idStr);

	try {
		id = stoi(idStr);
	}
	catch (invalid_argument&) {
		cout << "Id-ul trebuie sa fie un numar natural!\n\n";
		return;
	}

	try {
		activityService.deleteActivity(id);
		cout << "Activitate stearsa cu succes!\n";
	}
	catch (Exception& errorMessage)
	{
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::searchActivityUI() {
	int id;
	string idStr;
	cout << "Introduceti id:";
	getline(cin, idStr);

	try {
		id = stoi(idStr);
	}
	catch (invalid_argument&) {
		cout << "Id-ul trebuie sa fie un numar natural!\n\n";
		return;
	}

	try {
		const Activity& activity = activityService.findActivity(id);
		cout << activity << "\n";
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::updateActivityUI() {
	string idStr, durationStr, title, description, type;
	int id, duration;

	cout << "Introduceti id:";
	getline(cin, idStr);

	try {
		id = stoi(idStr);
	}
	catch (invalid_argument&) {
		cout << "Id-ul trebuie sa fie un numar natural!\n\n";
		return;
	}

	cout << "Introduceti titlu:";
	getline(cin, title);

	cout << "Introduceti descriere:";
	getline(cin, description);

	cout << "Introduceti tipul:";
	getline(cin, type);

	cout << "Introduceti durata:";
	getline(cin, durationStr);

	try {
		duration = stoi(durationStr);
	}
	catch (invalid_argument&) {
		cout << "Durata trebuie sa fie un numar natural!\n\n";
		return;
	}

	try {
		activityService.updateActivity(id,title,description,type,duration);
		cout << "Activitate modificata cu succes!\n";
	}
	catch (Exception& errorMessage)
	{
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::filterActivitiesUI() {
	cout << "1.Filtrare dupa descriere\n";
	cout << "2.Filtrare dupa tip\n";
	cout << ">>>";
	string option;
	getline(cin, option);
	if (option.length() == 1)
	{
		try {
			vector<Activity>result;
			switch (option[0]) {
			case '1':
			{
				string description;
				cout << "Introduceti descrierea:";
				getline(cin, description);
				if (description.length() == 0) {
					cout << "Descrierea trebuie sa contina cel putin un caracter!\n\n";
					return;
				}
				result = activityService.filterBy("description", description);
				break;
			}
			case '2':
			{
				string type;
				cout << "Introduceti tipul:";
				getline(cin, type);
				if (type.length() == 0) {
					cout << "Tipul trebuie sa contina cel putin un caracter!\n\n";
					return;
				}
				result = activityService.filterBy("type", type);
				break;
			}
			default:
				cout << "Optiune invalida!\n";
				break;
			}
			for (const auto& activity : result)
				cout << activity << endl;
		}
		catch (Exception& errorMessage) {
			cout << errorMessage.what();
		}
	}
	else
		cout << "Optiune invalida!\n";
	cout << endl;
}

void UI::sortActivitiesUI() {
	cout << "1.Sortare dupa titlu\n";
	cout << "2.Sortare dupa descriere\n";
	cout << "3.Sortare dupa tip si durata\n";
	cout << ">>>";
	string option;
	getline(cin, option);
	if (option.length() == 1)
	{
		vector<Activity> result;;
		switch (option[0]) {
		case '1':
		{
			result = activityService.sortBy(&Activity::compareByTitle);
			break;
		}
		case '2':
		{
			result = activityService.sortBy(&Activity::compareByDescription);
			break;
		}
		case '3':
		{
			result = activityService.sortBy(&Activity::compareByTypeAndDuration);
			break;
		}
		default:
			cout << "Optiune invalida!\n";
			break;
		}
		if(result.size()==0)
			cout << "Nu exista activitati!\n";
		for (const auto& activity : result)
			cout << activity << endl;
	}
	else
		cout << "Optiune invalida!\n";
	cout << endl;
}


void UI::printActivitiesUI() {
	try {
		const auto& activities = activityService.getAll();
		if (activities.size()) {
			for (const auto& activity : activities)
				cout << activity << endl;
			cout << endl;
		}
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what() << endl;
	}
}

void UI::showRaportUI() {
	try {
		auto raports = activityService.createMap();
		for (const auto& raport : raports) {
			cout << raport.second.getTitle() << ":" << raport.second.getFrequency() << "\n";
		}
	}
	catch (ServiceException& errorMessage) {
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::undoUI() {
	try {
		activityService.undo();
		cout << "Undo realizat cu succes!\n";
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what() << endl;
	}
	cout << endl;
}

void UI::runUI() {
	bool isRunning = true;
	while (isRunning) {
		printMenuUI();
		cout << ">>>";

		string option;
		getline(cin, option);

		if (option.length() == 1) {
			switch (option[0]) {
			case '1':
				addActivityUI();
				break;
			case '2':
				deleteActivityUI();
				break;
			case '3':
				updateActivityUI();
				break;
			case '4':
				searchActivityUI();
				break;
			case '5':
				filterActivitiesUI();
				break;
			case '6':
				sortActivitiesUI();
				break;
			case '7':
				printActivitiesUI();
				break;
			case '8':
				manageListUI();
				break;
			case '9':
				showRaportUI();
				break;
			case 'U':
				undoUI();
				break;
			case 'E':
				isRunning = false;
				break;
			default:
				cout << "Optiune invalida!\n" << endl;
				break;
			}
		}
		else {
			cout << "Optiune invalida!\n" << endl;
		}
	}
}

void UI::addActivityInListUI() {
	string title;
	cout << "Introduceti titlul activitatii:";
	getline(cin, title);
	try {
		activityService.addActivityInList(title);
		cout << "Activitate adaugata cu succes!\n";
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what();
	}
}

void UI::printActivityListUI() const {
	try {
		const auto& activities = activityService.getActivitiesFromList();
		if (activities.size()) {
			for (const auto& activity : activities)
				cout << activity << endl;
			cout << endl;
		}
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what();
	}
}

void UI::emptyListUI() {
	activityService.emptyActivityList();
	cout << "Lista s-a golit cu succes!\n";
}

void UI::exportListUI() const {
	string fileName;
	cout << "Introduceti numele fisierului unde va fi salvat:";
	getline(cin, fileName);
	if (fileName == "") {
		cout << "Numele fisierului este invalid!\n\n";
		return;
	}
	regex validFile(R"(.*\.csv$)");
	if (!regex_match(fileName, validFile)) {
		cout << "Fisierul trebuie sa aiba extensia .csv!\n";
		cout << endl;
		return;
	}
	try {
		activityService.exportListCSV(fileName);
		cout << "Lista a fost salvata cu succes!\n";
	}
	catch (Exception& errorMessage)
	{
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::generateActivitiesInListUI() {
	string numberStr;
	int number;
	cout << "Introduceti numarul de activitati de generat:";
	getline(cin, numberStr);
	try {
		number = stoi(numberStr);
	}
	catch (invalid_argument&) {
		cout << "Numarul trebuie sa fie natural!\n\n";
		return;
	}

	try {
		activityService.generateActivities(number);
		cout << "Activitati generate cu succes!\n";
	}
	catch (Exception& errorMessage) {
		cout << errorMessage.what();
	}
	cout << endl;
}

void UI::printListMenuUI() const {
	cout << "~Gestionare lista de activitati~\n";
	cout << "1.Adauga activitate\n";
	cout << "2.Genereaza activitati\n";
	cout << "3.Goleste lista\n";
	cout << "4.Export lista\n";
	cout << "5.Afiseaza lista\n";
	cout << "6.Inapoi\n";
}

void UI::manageListUI() {
	bool isRunning = true;
	cout << endl;
	while (isRunning) {
		printListMenuUI();
		cout << ">>>";
		string option;
		getline(cin, option);
		if (option.length() == 1)
		{
			switch (option[0]) {
			case '1':
				addActivityInListUI();
				try {
					cout << "Numarul de activitati din lista:" << activityService.getActivitiesFromList().size() << endl;
				}
				catch(Exception&){
					cout << "Numarul de activitati din lista: 0\n";
				}
				cout << endl;
				break;
			case '2':
				generateActivitiesInListUI();
				break;
			case '3':
				emptyListUI();
				try {
					cout << "Numarul de activitati din lista:" << activityService.getActivitiesFromList().size()<<endl;
				}
				catch (Exception&) {
					cout << "Numarul de activitati din lista: 0\n";
				}
				cout << endl;
				break;
			case '4':
				exportListUI();
				break;
			case '5':
				printActivityListUI();
				try {
					cout << "Numarul de activitati din lista:" << activityService.getActivitiesFromList().size() << endl;
				}
				catch (Exception&) {
					cout << "Numarul de activitati din lista: 0\n";
				}
				cout << endl;
				break;
			case '6':
				isRunning = false;
				break;
			default:
				cout << "Optiune invalida!\n"<<endl;
				break;
			}
		}
		else
			cout << "Optiune invalida!\n"<<endl;
	}
}

