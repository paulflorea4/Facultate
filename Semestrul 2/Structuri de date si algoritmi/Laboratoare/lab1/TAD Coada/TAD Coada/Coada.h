#pragma once
using namespace std;

typedef int TElem;

class Coada
{
	private:
		/* aici reprezentarea */
		TElem* elemente;
		int prim, ultim, lungime, capacitate;

		void redim();
	public:
		//constructorul implicit
		Coada();

		//adauga un element in coada
		void adauga(TElem e);

		//acceseaza elementul cel mai devreme introdus in coada 
		//arunca exceptie daca coada e vida
		TElem element() const;

		//sterge elementul cel mai devreme introdus in coada si returneaza elementul sters (principiul FIFO)
		//arunca exceptie daca coada e vida
		TElem sterge();

		//elimina si returneaza elementul minim din coada(presupunem ca elementele sunt numere intregi)
		//arunca exceptie in cazul in care coada este goala
		TElem stergeMinim();

		//verifica daca coada e vida;
		bool vida() const;


		// destructorul cozii
		~Coada();
};
