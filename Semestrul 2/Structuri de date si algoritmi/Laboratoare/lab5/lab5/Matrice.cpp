#include "Matrice.h"

#include <exception>

using namespace std;


Matrice::Matrice(int m, int n) {
	//BC=WC=AC -> theta(1)
	if (m <= 0 || n <= 0)
		throw exception();
	linii = m;
	coloane = n;
	radacina = nullptr;
}

Nod* Matrice::adaugaRec(Nod* nod, int i, int j, TElem val) {
	//BC:theta(1)
	//WC:theta(log n)
	//AC:O(log n)
	if (nod == nullptr)
		return new Nod(i, j, val);
	if (i < nod->linie || (i == nod->linie && j < nod->coloana))
		nod->stanga = adaugaRec(nod->stanga, i, j, val);
	else if (i > nod->linie || (i == nod->linie && j > nod->coloana))
		nod->dreapta = adaugaRec(nod->dreapta, i, j, val);
	else
		nod->valoare = val;
	return nod;
}

Nod* Matrice::cauta(Nod* nod, int i, int j) const {
	//BC:theta(1)
	//WC:theta(log n)
	//AC:O(log n)
	if (nod == nullptr)
		return nullptr;
	if (nod->linie == i && nod->coloana == j)
		return nod;
	if (i < nod->linie || (i == nod->linie && j < nod->coloana))
		return cauta(nod->stanga, i, j);
	else
		return cauta(nod->dreapta, i, j);
}

Nod* Matrice::sterge(Nod* nod, int i, int j, TElem& vechiVal) {
	//BC:theta(1)
	//WC:theta(log n)
	//AC:O(log n)
	if (nod == nullptr)
		return nullptr;

	if (i < nod->linie || (i == nod->linie && j < nod->coloana)) {
		nod->stanga = sterge(nod->stanga, i, j, vechiVal);
	}
	else if (i > nod->linie || (i == nod->linie && j > nod->coloana)) {
		nod->dreapta = sterge(nod->dreapta, i, j, vechiVal);
	}
	else {
		vechiVal = nod->valoare;

		if (nod->stanga == nullptr) {
			Nod* temp = nod->dreapta;
			delete nod;
			return temp;
		}
		else if (nod->dreapta == nullptr) {
			Nod* temp = nod->stanga;
			delete nod;
			return temp;
		}
		else {
			Nod* succ = nod->dreapta;
			while (succ->stanga != nullptr)
				succ = succ->stanga;

			nod->linie = succ->linie;
			nod->coloana = succ->coloana;
			nod->valoare = succ->valoare;

			nod->dreapta = sterge(nod->dreapta, succ->linie, succ->coloana, vechiVal);
		}
	}

	return nod;
}

int Matrice::nrLinii() const{
	//BC=WC=AC -> theta(1)
	return linii;
}


int Matrice::nrColoane() const{
	//BC=WC=AC -> theta(1)
	return coloane;
}


TElem Matrice::element(int i, int j) const {
	//BC=WC=AC -> theta(1)
	if (i < 0 || i >= linii || j < 0 || j >= coloane)
		throw exception();
	Nod* nod = cauta(radacina, i, j);
	return nod ? nod->valoare : NULL_TELEMENT;
}

TElem Matrice::modifica(int i, int j, TElem val) {
	//BC:theta(1)
	//WC:theta(log n)
	//AC:O(log n)
	if (i < 0 || i >= linii || j < 0 || j >= coloane)
		throw exception();
	Nod* nod = cauta(radacina, i, j);
	TElem vechi = NULL_TELEMENT;
	if (nod != nullptr) {
		vechi = nod->valoare;
		if (val == NULL_TELEMENT) {
			radacina = sterge(radacina, i, j, vechi);
		}
		else {
			nod->valoare = val;
		}
	}
	else {
		if (val != NULL_TELEMENT) {
			radacina = adaugaRec(radacina, i, j, val);
		}
	}
	return vechi;
}

void distruge(Nod* nod) {
	//BC:theta(1)
	//WC:theta(n)
	//AC:O(n)
	if (nod == nullptr)
		return;
	distruge(nod->stanga);
	distruge(nod->dreapta);
	delete nod;
}

Matrice::~Matrice() {
	//BC=WC=AC -> theta(1)
	distruge(radacina);
}

int valoareMaxRec(Nod* nod){
	//BC:theta(1)
	//WC:theta(n)
	//AC:theta(n)
	if (nod == nullptr)
		return NULL_TELEMENT;

	int st = valoareMaxRec(nod->stanga);
	int dr = valoareMaxRec(nod->dreapta);

	int maxim = nod->valoare;
	if (st > maxim)
		maxim = st;
	if (dr > maxim)
		maxim = dr;

	return maxim;

	/*subalgoritm valoareMaxRec(nod)
		daca nod e nil atunci
			returneaza NULL_TVALOARE
		sf daca

		st<-valoareMaxRec(nod.stanga)
		dr<-valoareMaxRec(nod.dreapta)

		maxim<-nod.valoare

		daca st<maxim atunci
			maxim<-st
		sf daca
		daca dr>maxim atunci
			maxim<-dr
		sf daca

		returneaza maxim;
	*/
}

int Matrice::valoareMaxima() const {
	//BC:theta(1)
	//WC:theta(n)
	//AC:theta(n)
	if (radacina == nullptr)
		return NULL_TELEMENT;
	return valoareMaxRec(radacina);

	/*
	* subalgoritm valoareMaxima(radacina)
	*		daca nod==nil atunci
	*			returneaza NULL_TVALOARE
	*		sf daca
	*		returneaza valoareMaxRec(radacina)
	*/
}

