
#include "Coada.h"
#include <exception>
#include <iostream>

using namespace std;


Coada::Coada() {
	capacitate = 10;
	elemente = new TElem[capacitate];
	prim = 0;
	ultim = 0;
	lungime = 0;
}

void Coada :: redim() {
	/*
	* Complexitate:theta(n)
	* BC=WC=AC
	* theta deoarece se executa mereu n pasi,unde n-lungimea cozii
	*/
	int nouaCapacitate = capacitate * 2;
	TElem* nouaLista = new TElem[nouaCapacitate];
	for (int i = 0; i < lungime; i++)
		nouaLista[i] = elemente[(prim + i) % capacitate];
	
	delete[] elemente;
	elemente = nouaLista;
	capacitate = nouaCapacitate;
	prim = 0;
	ultim = lungime;
}

void Coada::adauga(TElem elem) {
	/*
	* Complexitate
	* BC:lungime!=capacitate -> theta(1)
	* WC:lungime=capacitate->theta(n)
	* AC:O(n),n-lungimea cozii
	* O deoarece exista caz in care nu se executa operatia de redimensionare
	*/
	if (lungime == capacitate)
		redim();
	elemente[ultim] = elem;
	ultim = (ultim + 1) % capacitate;
	lungime++;
}

TElem Coada::element() const {
	/*
	* Complexitate theta(1)
	* BC=WC=AC
	* theta deoarece instructiunile se executa o singura data
	*/
	if (vida())
	{
		throw runtime_error("Coada este vida!");
	}
	return elemente[prim];
}

TElem Coada::sterge() {
	/*
	* Complexitate theta(1)
	* BC=WC=AC
	* theta deoarece instructiunile se executa o singura data
	*/
	if (vida())
	{
		throw runtime_error("Coada este vida!");
	}
	TElem elem = elemente[prim];
	prim = (prim + 1) % capacitate;
	lungime--;
	return elem;
}

bool Coada::vida() const {
	/*
	* Complexitate theta(1)
	* BC=WC=AC
	* theta deoarece instructiunile se executa o singura data
	*/
	return lungime == 0;
}

TElem Coada::stergeMinim() {
	/*
	* Complexitate theta(n),n lungimea cozii
	* BC=WC=AC:coada nu este goala =>se executa 2 for uri de n pasi => theta(2*n)=theta(n)
	*/
	if (vida()) {
		throw runtime_error("Coada este vida!");
	}

	TElem minim = element();
	int nrElemente = lungime,pozitie=0;
	Coada copieCoada;

	for (int i = 0; i < nrElemente; i++) {
		TElem elementCurent = sterge();
		if (elementCurent < minim) {
			pozitie = i;
			minim = elementCurent;
		}
		copieCoada.adauga(elementCurent);
	}
	for (int i = 0; i < nrElemente; i++)
	{
		if (pozitie != i)
			adauga(copieCoada.sterge());
		else
			copieCoada.sterge();
	}
	return minim;

	/*
	* PSEUDOCOD
	* stergeMinim(Coada)
		* daca vida()=true atunci
		*		arunca exceptie
		* sf daca
		* minim<-element(Coada)
		* nrElemente<-lungime
		* pozitie<-0
		* pentru i=0,nrElemente executa
		*		elementCurent<-sterge(Coada)
		*		daca elementCurent<minim atunci
		*			pozitie<-i
		*			minim<-elementCurent
		*		sf daca
		*		copieCoada.adauga(elementCurent)
		* sf pentru
		* 
		* pentru i=0,nrElemente executa
		*		daca pozitie!=i atunci
		*			adauga(copieCoada.sterge(Coada))
		*		altfel
		*			copieCoada.sterge(Coada)
		*		sf daca
		* sf pentru
	* returneaza minim
	*/
}

Coada::~Coada() {
	/*
	* Complexitate theta(1)
	* theta deoarece instructiunile se executa o singura data
	*/
	delete[] elemente;
}

