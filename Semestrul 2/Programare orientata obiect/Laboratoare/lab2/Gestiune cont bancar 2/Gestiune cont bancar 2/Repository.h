#pragma once
#include "Transaction.h"

typedef struct {
	Transaction** transactions;
	int length;
	int capacity;
}TransactionsRepository;

/*
* Functia creeaza un repository de tranzactii
* return:obiectul de tip TransactionsRepository
*/
TransactionsRepository* createTransactionsRepository();

/*
* Functia de adaugare a unei tranzactii in repository
* transaction:tranzactia de adaugat(Transaction)
* repository:repository-ul in care va fi adaugata tranzactia
* return:1 daca adaugarea s-a facut cu succes ,0 in caz contrar
*/
int addTransaction(Transaction* transaction, TransactionsRepository* repository);

/*
* Functia de modificare a unei tranzactii existente
* id:id-ul tranzactiei de actualizat(int)
* newTransaction:noua tranzactie(Transaction)
* repository:repository-ul de tranzactii
* return:1 daca tranzactia a fost modificata cu succes,0 in caz contrar
*/
int updateTransaction(Transaction* newTransaction, TransactionsRepository* repository);

/*
* Functia de stergere a unei tranzactii existente
* id:id-ul tranzactiei de sters(int)
* repository:repository-ul de tranzactii
* return:1 daca tranzactia a fost stearsa cu succes,0 in caz contrar
*/
int deleteTransaction(int id, TransactionsRepository* repository);

/*
* Functia returneaza numarul de tranzactii din repository
* repository:repository-ul de tranzactii
* return:lungimea vectorului de tranzactii(int)
*/
int getRepositoryLength(TransactionsRepository* repository);

/*
* Functia furnizeaza vectorul de tranzactii din repository
* repository:repository-ul de tranzactii
* list:lista furnizata
*/
Transaction** getTransactions(TransactionsRepository* repository);

/*
* Functia de dealocare a memoriei repository-ului dat ca parametru
* repository:repository-ul de dealocat
* return:-
*/
void destroyRepository(TransactionsRepository* repository);