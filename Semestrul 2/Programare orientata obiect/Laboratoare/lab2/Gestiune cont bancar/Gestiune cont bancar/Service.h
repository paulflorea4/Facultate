#pragma once
#include <stdio.h>
#include "Transaction.h"
#include "Repository.h"

typedef struct {
	TransactionsRepository repository;
}TransactionsService;

/*
* Functia de creare a unui service de tranzactii
* return:service-ul de tranzactii
*/
TransactionsService createTransactionsService();

/*
* Functia de adaugare a unei cheltuieli in service
* service:service-ul de tranzactii
* id-cheie unica(int)
* day-ziua tranzactiei(int)(1<=day<=31)
* sum-suma tranzactiei(float)(sum>0)
* type-tipul tranzactiei(1 sau 2)
* description-descrierea tranzactiei(pointer)
* return:0(adaugarea a fost facuta cu succes)
*		 1(id-ul este invalid)
*	     2(ziua este invalida)
*		 3(suma este invalida)
*		 4(tipul este invalid)
*		 5(exista deja o tranzactie cu id-ul dat ca parametru)
*/
int addTransactionService(TransactionsService* service,int id, int day, float sum, int type, char* description);

/*
* Functia de modificare a unei cheltuieli in service
* service:service-ul de tranzactii
* id-cheie unica(int)
* day-ziua tranzactiei(int)(1<=day<=31)
* sum-suma tranzactiei(float)(sum>0)
* type-tipul tranzactiei(1 sau 2)
* description-descrierea tranzactiei(pointer)
* return:0(adaugarea a fost facuta cu succes)
*		 1(id-ul este invalid)
*	     2(ziua este invalida)
*		 3(suma este invalida)
*		 4(tipul este invalid)
*		 5(nu exista o tranzactie cu id-ul dat ca parametru)
*/
int updateTransactionService(TransactionsService* service, int id, int day, float sum, int type, char* description);

/*
* Functia de stergere a unei cheltuieli in service
* service:service-ul de tranzactii
* id-cheie unica(int)
* return:1 daca tranzactia exista,0 in caz contrar
*/
int deleteTransactionService(TransactionsService* service, int id);

/*
* Functia returneaza numarul de tranzactii din service-ul de tranzactii
* service:service-ul de tranzactii
* return:numarul de tranzactii(int)
*/
int getTransactionsLength(TransactionsService* service);

/*
* Functia furnizeaza lista de tranzactii din service-ul de tranzactii
* service:service-ul de tranzactii
* length:referinta catra lungimea listei de tranzactii
* list:lista furnizata
*/
void getTransactionsService(int* length, TransactionsService* service,Transaction* list);

/*
* Functia de filtrare a listei de tranzactii din service
* type:tipul dupa care se face filtrarea(int)
* length:lungimea listei filtrate
* service:service-ul de tranzactii
* filteredList:lista de filtrat
*/
void getTransactionsFilteredByType(int type, int* length, TransactionsService* service, Transaction* filteredList);

/*
* Functia de filtrare a listei de tranzactii din service
* type:suma dupa care se face filtrarea(int)
* length:lungimea listei filtrate
* property:proprietatea dupa care se face filtrarea(1 tranzactiile cu suma mai mica ca suma data,0 tranzactiile cu suma mai mare sau egala ca suma data)
* service:service-ul de tranzactii
* filteredList:lista de filtrat
*/
void getTransactionsFilteredBySum(float sum, int* length, int property, TransactionsService* service, Transaction* filteredList);

/*
*Functia de sortare a listei de tranzactii dupa suma
* ascending:modul in care este ordonata lista(1 pentru crescator,0 pentru descrescator)
* service:service-ul de tranzactii
* sortedList:lista de sortat
*/
void getTransactionsSortedBySum(int ascending, TransactionsService* service,Transaction* sortedList);

/*
*Functia de sortare a listei de tranzactii dupa zi
* ascending:modul in care este ordonata lista(1 pentru crescator,0 pentru descrescator)
* service:service-ul de tranzactii
* sortedList:lista de sortat
*/
void getTransactionsSortedByDay(int ascending, TransactionsService* service,Transaction* sortedList);

