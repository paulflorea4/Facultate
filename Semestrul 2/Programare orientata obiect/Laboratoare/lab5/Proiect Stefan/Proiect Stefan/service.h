#pragma once
#include <stdio.h>
#include "domain.h"
#include "list.h"

typedef struct {
	List* repository;
	List* undoList;
	List* deletedList;
}service;

// Valideaza o materie prima
// m - materie prima
// return 1 - daca materie prima este valida
// return 0 - daca materie prima nu este valida
int valideazaMateriePrima(materiePrima* m);
// Adauga o materie prima in repo
// r - repo
// nume - nume materie prima
// producator - producator materie prima
int addMaterieService(service* s, char* nume, char* producator, int cantitate);
// Modifica o materie prima din repo
// r - repo
// nume - nume materie prima
// producator - producator materie prima
// cantitate - cantitate materie prima
materiePrima* getMateriePrima(service* s, int poz);
// Modifica o materie prima din repo
// r - repo
// nume - nume materie prima
// producator - producator materie prima
// cantitate - cantitate materie prima
// return 1 - daca modificarea a fost efectuata
// return 0 - daca modificarea nu a fost efectuata
int modificaMaterieService(service* s, char* nume, char* producator, int cantitate);
// Sterge o materie prima din repo
// r - repo
// nume - nume materie prima
// return 1 - daca stergerea a fost efectuata
// return 0 - daca stergerea nu a fost efectuata
int stergeMaterieService(service* s, char* nume);
// Creeaza un repo
// return repo
service* creeazaRepoService();
// Filtrare dupa litera
// sursa - repo sursa
// litera - litera dupa care se filtreaza
// return repo filtrat
List* filtrareDupaLitera(service* sursa, char litera);
// Filtrare dupa cantitate
// sursa - repo sursa
// cantitate - cantitate dupa care se filtreaza
// return repo filtrat
List* filtrareDupaCantitate(service* sursa, int cantitate);
// Filtrare dupa producator
// sursa - repo sursa
// cantitate - producatorul dupa care se filtreaza
// return repo filtrat
List* filtrareDupaProducator(service* sursa, char* producator);
// Genereaza materii prime
// r - repo
void generareMateriiPrime(service* s);
// Sorteaza repo-ul
// r - repo
// cmp - functie de comparare
void sortare(service* s, int(*cmp)(materiePrima*, materiePrima*));
// Compara doua materii prime dupa nume
// m1 - materie prima 1
// m2 - materie prima 2
// return 1 - daca m1 > m2
// return -1 - daca m1 < m2
// return 0 - daca m1 = m2
int cmpNume(materiePrima* m1, materiePrima* m2);
// Compara doua materii prime dupa cantitate
// m1 - materie prima 1
// m2 - materie prima 2
// return 1 - daca m1 > m2
// return -1 - daca m1 < m2
// return 0 - daca m1 = m2
int cmpCantitate(materiePrima* m1, materiePrima* m2);
// Compara doua materii prime dupa nume descrescator
// m1 - materie prima 1
// m2 - materie prima 2
// return 1 - daca m1 > m2
// return -1 - daca m1 < m2
// return 0 - daca m1 = m2
int cmpNumeDesc(materiePrima* m1, materiePrima* m2);
// Compara doua materii prime dupa cantitate descrescator
// m1 - materie prima 1
// m2 - materie prima 2
// return 1 - daca m1 > m2
// return -1 - daca m1 < m2
// return 0 - daca m1 = m2
int cmpCantitateDesc(materiePrima* m1, materiePrima* m2);
//distruge service-ul dat ca parametru
//s-pointer la service
void distrugeService(service* s);
//functia de undo
//s-pointer la service
//return 1 - daca nu se mai poate face undo
//return 0 - in caz contrar
int undo(service* s);
