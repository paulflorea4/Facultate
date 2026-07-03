#include "Repository.h"
#include <fstream>
#include <sstream>
#include <algorithm>
using namespace std;

void Repository::load() {
	ifstream fin(fisier);

	string linie;
	while (getline(fin >> ws, linie)) {
		istringstream ss(linie);
		string adresa, linii, coloane, stare;
		getline(ss, adresa, ' ');
		getline(ss, linii, ' ');
		getline(ss, coloane, ' ');
		getline(ss, stare, ' ');

		parcari.push_back(Parcare(adresa, stoi(linii), stoi(coloane), stare));
	}
	fin.close();
	sort(parcari.begin(), parcari.end(), [](const Parcare& p1, const Parcare& p2) {
		return p1.getLinii() * p1.getColoane() < p2.getLinii() * p2.getColoane();
		});
}

void Repository::save() {
	ofstream fout(fisier);
	for (const auto& p : parcari)
		fout << p.getAdresa() << " " << p.getLinii() << " " << p.getColoane() << " " << p.getStare() << endl;
	fout.close();
}

void Repository::add(const Parcare& p) {
	string adresa = p.getAdresa();
	auto it = find_if(parcari.begin(), parcari.end(), [&adresa](const Parcare& parcare) {
		return parcare.getAdresa() == adresa;
		});
	if (it == parcari.end()) {
		parcari.push_back(p);
		save();
	}
}

void Repository::update(const Parcare& p) {
	string adresa = p.getAdresa();
	auto it = find_if(parcari.begin(), parcari.end(), [&adresa](const Parcare& parcare) {
		return parcare.getAdresa() == adresa;
		});
	if (it != parcari.end()) {
		*it = p;
		save();
	}
}

vector<Parcare>& Repository::getAll() {
	sort(parcari.begin(), parcari.end(), [](const Parcare& p1, const Parcare& p2) {
		return p1.getLinii() * p1.getColoane() < p2.getLinii() * p2.getColoane();
		});
	return parcari;
}

