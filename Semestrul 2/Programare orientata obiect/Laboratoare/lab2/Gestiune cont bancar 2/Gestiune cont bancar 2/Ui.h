#pragma once
#include "Service.h"
#include <stdbool.h>

/*
* Functia principala a aplicatiei care dirijeaza intreaga executie
*/
void runApp();

/*
* Functia afiseaza meniul utiliaztorului
*/
void printMenu();

/*
* Functia de citire a unui numar intreg de la tastatura
* return:-1 daca input-ul nu este numar natural,valoarea citita in caz contrar
*/
int getIntegerFromUser();

/*
* Functia de citire a unui numar real de la tastatura
* return:-1 daca input-ul nu este numar real pozitiv,valoarea citita in caz contrar
*/
int getFloatFromUser();

/*
* Functia de citire a id-ului unei tranzactii
* id:referinta catre id-ul tranzactiei
* return:true daca id-ul este valid,false in caz contrar
*/
bool readIDFromUser(int* id);

/*
* Functia de citire a zilei unei tranzactii
* day:referinta catre ziua tranzactiei
* return:true daca ziua este valida,false in caz contrar
*/
bool readDayFromUser(int* day);

/*
* Functia de citire a zilei unei tranzactii
* day:referinta catre ziua tranzactiei
* return:true daca ziua este valida,false in caz contrar
*/
bool readSumFromUser(float* sum);

/*
* Functia de citire a tipului unei tranzactii
* type:referinta catre tipul tranzactiei
* return:true daca tipul este valid,false in caz contrar
*/
bool readTypeFromUser(int* type);

/*
* Functia de citire a descrierii unei tranzactii
* description:referinta catre descrierea tranzactiei
* return:-
*/
void readDescriptionFromUser(char* description);

/*
* Functia preia datele utilizatorului si adauga tranzactia cu datele introduse daca acestea sunt valide
* service:service-ul de tranzactii
* return:-
*/
void addTransactionUI(TransactionsService* service);

/*
* Functia preia datele utilizatorului si modifca tranzactia cu datele introduse daca acestea sunt valide
* service:service-ul de tranzactii
* return:-
*/
void updateTransactionUI(TransactionsService* service);

/*
* Functia preia id-ul tranzactiei si sterge tranzactia cu id-ul introdus daca exista
* service:service-ul de tranzactii
* return:-
*/
void deleteTransactionUI(TransactionsService* service);

/*
* Functia de afisare a listei de tranzactii din service
* service:service-ul de tranzactii
* return:-
*/
void printTransactions(TransactionsService* service);

/*
* Functia de gestionare a optiunii introduse de utilizator asupra service-lui de tranzactii
* option:optiunea utilizatorului(int)
* service:service-ul de tranzactii
*/
void handleInput(int option, TransactionsService* service);

/*
* Functia de afisare a listei de tranzactii filtrata dupa filtrele introduse de utilizator
* service:service-ul de tranzactii
* return:-
*/
void printFilteredTransactions(TransactionsService* service);

/*
* Functia de afisare a listei de tranzactii sortata dupa proprietatile introduse de utilizator
* service:service-ul de tranzactii
* return:-
*/
void printSortedTransactions(TransactionsService* service);




