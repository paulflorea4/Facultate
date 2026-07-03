#pragma once

typedef int TElem;


#define NULL_TELEMENT 0

struct Nod {
	int linie;
	int coloana;
	TElem valoare;
	Nod* stanga;
	Nod* dreapta;

	Nod(int l, int c, TElem val) : linie(l), coloana(c), valoare(val), stanga(nullptr), dreapta(nullptr) {}
};

class Matrice {

private:
	Nod* radacina;
	int linii;
	int coloane;

	Nod* adaugaRec(Nod* nod, int i, int j, TElem val);
	Nod* cauta(Nod* nod, int i, int j) const;
	Nod* sterge(Nod* nod, int i, int j, TElem& vechiVal);

public:

	//constructor
	//se arunca exceptie daca nrLinii<=0 sau nrColoane<=0
	Matrice(int nrLinii, int nrColoane);


	//destructor
	~Matrice();

	//returnare element de pe o linie si o coloana
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	//indicii se considera incepand de la 0
	TElem element(int i, int j) const;


	// returnare numar linii
	int nrLinii() const;

	// returnare numar coloane
	int nrColoane() const;


	// modificare element de pe o linie si o coloana si returnarea vechii valori
	// se arunca exceptie daca (i,j) nu e o pozitie valida in Matrice
	TElem modifica(int i, int j, TElem);

	int valoareMaxima() const;

};







