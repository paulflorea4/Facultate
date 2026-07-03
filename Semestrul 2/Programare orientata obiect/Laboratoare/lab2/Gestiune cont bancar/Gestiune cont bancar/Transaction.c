#define _CRT_SECURE_NO_WARNINGS
#include "Transaction.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

Transaction createTransaction(int id, int day, float sum, int type, char* description)
{
	Transaction transaction;
	transaction.id = id;
	transaction.day = day;
	transaction.sum = sum;
	transaction.type = type;
	strcpy(transaction.description, description);
	return transaction;
}

int validateTransaction(Transaction transaction)
{
	if (transaction.id < 0)
		return 1;
	if (transaction.day < 0 || transaction.day>31)
		return 2;
	if (transaction.sum < 0)
		return 3;
	if (transaction.type!=1 && transaction.type!=2)
		return 4;
	return 0;
}

char* getTypeString(Transaction* transaction)
{
	if (transaction->type == 1)
		return "Intrare";
	else if (transaction->type == 2)
		return "Iesire";
	else return "Invalid";
}

void printTransactionStringFormat(Transaction* transaction)
{
	printf("ID: %d | Ziua: %d | Suma: %.2f | Tipul: %s | Descriere: %s", transaction->id, transaction->day,
		transaction->sum, getTypeString(transaction), transaction->description);
}
