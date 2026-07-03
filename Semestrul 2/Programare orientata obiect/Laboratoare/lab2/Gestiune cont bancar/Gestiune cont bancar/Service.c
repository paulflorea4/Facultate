#include <stdio.h>
#include <stdlib.h>
#include "Transaction.h"
#include "Repository.h"
#include "Service.h"

TransactionsService createTransactionsService()
{
	TransactionsService service;
	service.repository = createTransactionsRepository();
	return service;
}

int addTransactionService(TransactionsService* service,int id, int day, float sum, int type, char* description)
{
	Transaction transaction=createTransaction(id,day,sum,type,description);
	int returnCode = validateTransaction(transaction);
	if (returnCode != 0)
	{
		return returnCode;
	}
	if (addTransaction(transaction, &service->repository) == 0)
		return 5;
	return 0;
}

int updateTransactionService(TransactionsService* service, int id, int day, float sum, int type, char* description)
{
	Transaction newTransacation=createTransaction(id,day,sum,type,description);
	int returnCode = validateTransaction(newTransacation);
	if (returnCode != 0)
	{
		return returnCode;
	}
	if(updateTransaction(newTransacation, &service->repository)==0)
		return 5;
	return 0;
}


int deleteTransactionService(TransactionsService* service, int id)
{
	return deleteTransaction(id, &service->repository);
}

int getTransactionsLength(TransactionsService* service)
{
	return getRepositoryLength(&service->repository);
}

void getTransactionsService(int* length, TransactionsService* service,Transaction* list)
{
	*length = getRepositoryLength(&service->repository);
	getTransactions(&service->repository, list);
}

void getTransactionsFilteredByType(int type, int* length, TransactionsService* service, Transaction* filteredList)
{
	if (type != 1 && type != 2)
	{
		printf("Tipul este invalid!\n");
		return;
	}

	getTransactionsService(length, service,filteredList);

	for (int i = 0; i < *length; i++)
	{
		if (filteredList[i].type != type)
		{
			for (int j = i; j < *length - 1; j++)
				filteredList[j] = filteredList[j + 1];
			(*length)--;
			i--;
		}
	}
	if (*length == 0)
		printf("Nu exista tranzactii!\n");
}

void getTransactionsFilteredBySum(float sum, int* length, int property, TransactionsService* service, Transaction* filteredList)
{
	if (sum < 0)
	{
		printf("Suma trebuie sa fie strict pozitiva!\n");
		return;
	}
	
	getTransactionsService(length, service, filteredList);
	if (property)
	{
		for (int i = 0; i < *length; i++)
		{
			if (filteredList[i].sum >= sum)
			{
				for (int j = i; j < *length - 1; j++)
					filteredList[j] = filteredList[j + 1];
				(*length)--;
				i--;
			}
		}
	}
	else
	{
		for (int i = 0; i < *length; i++)
		{
			if (filteredList[i].sum < sum)
			{
				for (int j = i; j < *length - 1; j++)
					filteredList[j] = filteredList[j + 1];
				(*length)--;
				i--;
			}
		}
	}
	
	if (*length == 0)
		printf("Nu exista tranzactii!\n");
}

void getTransactionsSortedBySum(int ascending, TransactionsService* service, Transaction* sortedList)
{
	int length=0;
	getTransactionsService(&length, service, sortedList);

	if (length == 0)
	{
		printf("Nu exista tranzactii!\n");
		return;
	}

	Transaction aux;

	if (ascending)
	{
		for(int i=0;i<length;i++)
			for (int j = i + 1; j < length; j++)
			{
				if (sortedList[i].sum > sortedList[j].sum)
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}
	else
	{
		for (int i = 0; i < length; i++)
			for (int j = i + 1; j < length; j++)
			{
				if (sortedList[i].sum < sortedList[j].sum)
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}
}

void getTransactionsSortedByDay(int ascending, TransactionsService* service, Transaction* sortedList)
{
	int length=0;
	getTransactionsService(&length, service, sortedList);

	if (length == 0)
	{
		printf("Nu exista tranzactii!\n");
		return;
	}

	Transaction aux;

	if (ascending)
	{
		for (int i = 0; i < length; i++)
			for (int j = i + 1; j < length; j++)
			{
				if (sortedList[i].day > sortedList[j].day)
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}
	else
	{
		for (int i = 0; i < length; i++)
			for (int j = i + 1; j < length; j++)
			{
				if (sortedList[i].day < sortedList[j].day)
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}
}
