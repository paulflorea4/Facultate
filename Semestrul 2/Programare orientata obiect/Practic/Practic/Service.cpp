#include "Service.h"

void Service::add(string adresa, int linii, int coloane, string stare) {
	repository.add(Parcare{ adresa,linii,coloane,stare });
	notify();
}

int Service::update(string adresa, int linii, int coloane, string stare) {
	if (linii < 1 || linii>5)
		return 1;
	if (coloane < 1 || coloane>5)
		return 2;
	if (stare.size() != linii * coloane)
		return 3;
	for (char c : stare) {
		if (c != 'X' && c != '-')
			return 4;
	}

	repository.update(Parcare(adresa, linii, coloane, stare));
	notify();
	return 0;

}