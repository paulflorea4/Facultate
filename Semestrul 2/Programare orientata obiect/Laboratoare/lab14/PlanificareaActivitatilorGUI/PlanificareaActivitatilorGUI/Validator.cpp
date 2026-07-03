#include "Validator.h"
#include <stdexcept>
#include <regex>
using std::regex;

bool Validator::validateActivity(const Activity& activity) {
	string errorMessage = "";
	if (activity.getId() <= 0)
		errorMessage += "Id-ul trebuie sa fie numar natural strict pozitiv!\n";
	if (activity.getTitle() == "")
		errorMessage += "Titlul trebuie sa contina cel putin un caracter!\n";
	if(activity.getDescription()=="")
		errorMessage+= "Descrierea trebuie sa contina cel putin un caracter!\n";
	if(activity.getType()=="")
		errorMessage += "Tipul trebuie sa contina cel putin un caracter!\n";
	if(activity.getDuration()<=0)
		errorMessage+= "Durata trebuie sa fie numar natural strict pozitiv!\n";
	
	if (errorMessage != "")
		throw ValidatorException(errorMessage);
	return true;
}

bool Validator::validateInt(const string& numberStr) {
	int number;
	try {
		number = std::stoi(numberStr);
		return true;
	}
	catch (std::invalid_argument&) {
		throw ValidatorException("Numarul trebuie sa fie natural!\n");
	}
}

bool Validator::validateFile(const string& filename) {
	regex validFile(R"(.*\.csv$)");
	if (!regex_match(filename, validFile)) {
		throw ValidatorException("Fisierul trebuie sa aiba extensia .csv!\n");
	}
	return true;
}