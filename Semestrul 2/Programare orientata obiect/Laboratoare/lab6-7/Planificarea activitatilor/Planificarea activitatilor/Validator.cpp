#include "Validator.h"

bool Validator::validateActivity(const Activity& activity) {
	string errorMessage = "";
	if (activity.getId() < 0)
		errorMessage += "Id-ul trebuie sa fie numar pozitiv!\n";
	if (activity.getTitle() == "")
		errorMessage += "Titlul trebuie sa contina cel putin un caracter!\n";
	if(activity.getDescription()=="")
		errorMessage+= "Descrierea trebuie sa contina cel putin un caracter!\n";
	if(activity.getType()=="")
		errorMessage += "Tipul trebuie sa contina cel putin un caracter!\n";
	if(activity.getDuration()<=0)
		errorMessage+= "Durata trebuie sa fie strict pozitiva!\n";
	
	if (errorMessage != "")
		throw ValidatorException(errorMessage);
	return true;
}