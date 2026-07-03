#pragma once

#include "Activity.h"
#include "Exception.h"
#include <string>

using std::string;

class ValidatorException :public Exception {
public:
	explicit ValidatorException(string message) :Exception(message) {}
};

class Validator {
public:
	//Functia de validare a unei activitati date ca parametru
	//activity:referinta catre activitatea de validat
	//return:true daca activitatea este valida
	bool static validateActivity(const Activity& activity);

	bool static validateInt(const string& numberStr);

	bool static validateFile(const string& filename);
};