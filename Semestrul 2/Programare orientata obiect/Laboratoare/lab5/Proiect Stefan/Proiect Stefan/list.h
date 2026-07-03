#pragma once
#include "domain.h"

typedef void* TElem;
typedef void (*functieDistrugere)(TElem);
typedef TElem(*functieCopiere)(TElem);
typedef struct {
	TElem* elemente;
	int len;
	int capacitate;
	functieCopiere functieCopiere;
	functieDistrugere functieDistrugere;
}List;

//functia de creare a unei liste
//functieCopiere-functia de copiere a elementelor listei
//functieDistrugere-functia de distrugere a elementelor listei
//return-pointer la lista creata
List* creeazaLista(functieCopiere functieCopiere, functieDistrugere functieDistrugere);

//functia de distrugere a unei liste date ca parametru
//l-pointer la list care urmeaza sa fie distrusa
void distrugeLista(List* l);

//functia de copiere a unei liste date ca parametru
//l-pointer la list care urmeaza sa fie copiata
//return-pointer la copia listei
List* copiazaLista(List* l);

//functia returneaza nr de elemente din lista data ca parametru
//l-pointer la lista
//return-nr de elemente din lista(int)
int getLen(List* l);

//functia de adaugare a unui element in lista
//l-pointer la lista
//element-elementul de adaugat
void add(List* l, TElem element);

//functia de actualizarea unui element din lista de materii data ca parametru
//l-pointer catre lista de materii
//return 1:daca s-a putut face actualizarea
//		 0:in caz contrar
int updateMaterie(List* l, TElem elementNou);

//functia de stergere a unui element din lista de materii
//l-pointer catre lista de materii
//nume-numele materiei prime de sters
//return 1:daca s-a sters materia prima
//       0:in caz contrar
int stergeMaterie(List* l, char* nume);

//functia de verificare a existentei unei materii in lista de materii prime
//l-pointer catre lista de materii
//nume-numele materiei de verificat
//return 1:daca materia exista in lista
//       0:in caz contrar
int verificaExistenta(List* l, char* nume);

//functia de obtinere a unui element de pe o pozitie data
//l-pointer catre lista de materii
//poz-pozitia din lista
//return: elementul de pe pozitia poz daca poz e valid
//		  0 altfel
TElem getMateriePrimaRepo(List* l, int poz);

//functia de obtinere a unui element din lista dupa nume
//l-pointer catre lista de materii
//nume-numele materiei prime
//return: elementul cu numele dat daca exista
//		  NULL altfel
materiePrima* getMaterieDupaNume(List* l, char* nume);

//functia de returnare si stergere a ultimului element din lista
//l-pointer catre lista de materii
//return:ultimul element din lista data ca parametru
TElem pop(List* l);

