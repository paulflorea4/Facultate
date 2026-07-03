#pragma once
/*
* Structura unei tranzactii
* id-cheie unica(int)
* day-ziua tranzactiei(int)(1<=day<=31)
* sum-suma tranzactiei(float)(sum>0)
* type-tipul tranzactiei(1 sau 2)
* description-descrierea tranzactiei(string)
*/
typedef struct {
	int id;
	int day;
	float sum;
	int	type;
	char* description;
}Transaction;

/*
* Functia creeaza o tranzactie noua cu parametrii transmisi
* id-cheie unica(int)
* day-ziua tranzactiei(int)(1<=day<=31)
* sum-suma tranzactiei(float)(sum>0)
* type-tipul tranzactiei(1 sau 2)
* description-descrierea tranzactiei(string)
* return:obiect de tip Transaction
*/
Transaction* createTransaction(int id, int day, float sum, int type, char* description);

/*
* Functia valideaza tranzactia data ca parametru
* transaction:obiect de tip Transaction
* return:0(daca tranzactia este valida)
*		 1(id-ul este invalid)
*		 2(ziua este invalida)
*        3(suma este invalida)
*		 4(tipul este invalid)
*/
int validateTransaction(Transaction transaction);

/*
* Functia returneaza tipul tranzactiei subforma de string
* transaction:referinta tranzactiei
* return:tipul tranzactiei(string)
*		Intrare=1
*		Iesire=2
*/
void printTransactionStringFormat(Transaction* transaction);

/*
* Functia afiseaza o tranzactie sub forma unui string
* transaction:referinta tranzactiei
* return:-
*/
char* getTypeString(Transaction* transaction);

/*
* Functia de dealocare a tranzactiei date ca parametru
* transaction:referinta tranzactiei
* return:-
*/
void destroyTransaction(Transaction* transaction);


