#pragma once
#include <stdio.h>
#include "Transaction.h"
#include "Repository.h"

typedef struct {
	TransactionsRepository* repository;
}TransactionsService;

/*
* Functia de creare a unui service de tranzactii
* return:service-ul de tranzactii
*/
TransactionsService* createTransactionsService();

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
int addTransactionService(TransactionsService* service, int id, int day, float sum, int type, char* description);

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
* Functia de copiere a unei liste de tranzactii data ca parametru
* list:lista de copiat
* length:lungimea listei
* return:copia listei
*/
Transaction** copyList(Transaction** list, int length);

/*
* Functia furnizeaza lista de tranzactii din service-ul de tranzactii
* service:service-ul de tranzactii
* length:referinta catra lungimea listei de tranzactii
* return:lista de tranzactii
*/
Transaction** getTransactionsService(int* length, TransactionsService* service);

/*
* Functia de filtrare a listei de tranzactii din service
* type:tipul dupa care se face filtrarea(int)
* length:lungimea listei filtrate
* service:service-ul de tranzactii
* return:NULL daca nu exista tipul nu este valid sau lista filtrata este goala,lista filtrata altfel
*/
Transaction** getTransactionsFilteredByType(int type, int* length, TransactionsService* service);

/*
* Functia de filtrare a listei de tranzactii din service
* type:suma dupa care se face filtrarea(int)
* length:lungimea listei filtrate
* property:proprietatea dupa care se face filtrarea(1 tranzactiile cu suma mai mica ca suma data,0 tranzactiile cu suma mai mare sau egala ca suma data)
* service:service-ul de tranzactii
* return:NULL daca nu exista suma nu este valida sau lista filtrata este goala,lista filtrata altfel
*/
Transaction** getTransactionsFilteredBySum(float sum, int* length, int property, TransactionsService* service); 

/*
* Functia de comparare a sumelor a 2 tranzactii date ca parametru
* transaction1:referinta catre prima tranzactie
* transaction2:referinta catre a doua tranzactie
* return:1 daca suma primei tranzactii este strict mai mare decat suma celeilalte tranzactii,0 in caz contrar
*/
int compareSum(Transaction * transaction1, Transaction * transaction2);

/*
* Functia de comparare a zilelor a 2 tranzactii date ca parametru
* transaction1:referinta catre prima tranzactie
* transaction2:referinta catre a doua tranzactie
* return:1 daca ziua primei tranzactii este strict mai mare decat ziua celeilalte tranzactii,0 in caz contrar
*/
int compareDay(Transaction* transaction1, Transaction* transaction2);

/*
*Functia de sortare a listei de tranzactii dupa suma
* ascending:modul in care este ordonata lista(1 pentru crescator,0 pentru descrescator)
* service:service-ul de tranzactii
* return:NULL daca lista de tranzactii e goala,lista sortata altfel
*/
Transaction** getTransactionsSortedBySum(int ascending, TransactionsService* service, int (*compareSum)(Transaction*, Transaction*));

/*
*Functia de sortare a listei de tranzactii dupa zi
* ascending:modul in care este ordonata lista(1 pentru crescator,0 pentru descrescator)
* service:service-ul de tranzactii
* return:NULL daca lista de tranzactii e goala,lista sortata altfel
*/
Transaction** getTransactionsSortedByDay(int ascending, TransactionsService* service, int (*compareDay)(Transaction*, Transaction*));

/*
* Functia de dealocare a service-ului
* service:service-ul de dealocat
* return:-
*/
void destroyService(TransactionsService* service);

