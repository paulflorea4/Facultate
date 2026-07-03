#pragma once
#include <string>
using std::string;

class Parcare {
private:
	string adresa;
	int linii, coloane;
	string stare;
public:
	Parcare(string adresa,int linii,int coloane,string stare):adresa{adresa},linii{linii},coloane{coloane},stare{stare}{}
	
	//returneaza adresa parcarii
	string getAdresa() const { return adresa; }
	//returneaza numarul de linii ale parcarii
	int getLinii() const { return linii; }
	//returneaza numarul de coloane ale parcarii
	int getColoane() const { return coloane; }
	//returneaza starea parcarii
	string getStare() const { return stare; }

	//seteaza o noua stare pentru parcare
	void setStare(string stare) { this->stare = stare; }
};
