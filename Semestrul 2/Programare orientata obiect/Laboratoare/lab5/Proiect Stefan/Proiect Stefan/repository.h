//#pragma once
//#include "domain.h"
//#include "list.h"
//
// adauga o materie prima in repo
// r - pointer la repo
// m - pointer la materie prima
//void addMateriePrima(List*, materiePrima*);
// creeaza un repo
// returneaza un pointer la repo
//repo* creeazaRepo(functieCopiere functieCopiere,functieDistrugere functieDistrugere);
// distruge un repo
// r - pointer la repo
//void distrugeRepo(List*);
// returneaza lungimea unui repo
// r - pointer la repo
// returneaza lungimea repo-ului
//int getLen(List*);
// modifica o materie prima
// r - pointer la repo
// nume - numele materiei prime
// producator - producatorul materiei prime
// cantitate - cantitatea de materie prima
// returneaza 1 daca modificarea a fost efectuata cu succes, 0 altfel
//int modificaMaterie(List* l, char* nume, char* producator, int cantitate);
// sterge o materie prima
// r - pointer la repo
// nume - numele materiei prime
// returneaza 1 daca stergerea a fost efectuata cu succes, 0 altfel
//int stergeMaterie(List* l, char* nume);
// verifica existenta unei materii prime
// r - pointer la repo
// nume - numele materiei prime
// returneaza 1 daca materie prima exista, 0 altfel
//int verificaExistenta(List* l, char* nume);
/// copiaza repository-ul dat ca parametru
/// lista-pointer la un obiect de tip repository
/// returneaza un pointer la copia listei date ca parametru
//repo* copiazaLista(repo* lista);
// returneaza materia prima de pe o pozitie data
// r - pointer la repo
// poz - pozitia materiei prime
// returneaza pointer la materie prima
//materiePrima* getMateriePrimaRepo(List* l, int poz);
