#pragma once
#include <stdlib.h>


// definirea structurii materiePrima
// nume - numele materiei prime
// producator - producatorul materiei prime
// cantitate - cantitatea de materie prima
typedef struct {
	char* nume;
	char* producator;
	int cantitate;
}materiePrima;


// creeaza o materie prima
// nume - numele materiei prime
// producator - producatorul materiei prime
// cantitate - cantitatea de materie prima
// returneaza un pointer la o structura materiePrima
materiePrima* creeazaMateriePrima(char* nume, char* producator, int cantitate);

// distruge o materie prima
// m - pointer la o structura materiePrima
void distrugeMateriePrima(materiePrima* m);
// copiaza o materie prima
// m - pointer la o structura materiePrima
// returneaza un pointer la o structura materiePrima
materiePrima* copyMateriePrima(materiePrima* m);