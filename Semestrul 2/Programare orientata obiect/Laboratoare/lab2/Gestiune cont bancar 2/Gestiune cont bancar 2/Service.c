#include <stdio.h>
#include <stdlib.h>
#include "Transaction.h"
#include "Repository.h"
#include "Service.h"

TransactionsService* createTransactionsService()
{
	TransactionsService* service=malloc(sizeof(TransactionsService));
	if (service != NULL)
		service->repository = createTransactionsRepository();
	return service;
}

int addTransactionService(TransactionsService* service, int id, int day, float sum, int type, char* description)
{
	Transaction* transaction = createTransaction(id, day, sum, type, description);
	int returnCode = validateTransaction(*transaction);
	if (returnCode != 0)
	{
		destroyTransaction(transaction);
		return returnCode;
	}
	if (addTransaction(transaction, service->repository) == 0)
	{
		destroyTransaction(transaction);
		return 5;
	}
	return 0;
}

int updateTransactionService(TransactionsService* service, int id, int day, float sum, int type, char* description)
{
	Transaction* newTransaction = createTransaction(id, day, sum, type, description);
	int returnCode = validateTransaction(*newTransaction);
	if (returnCode != 0)
	{
		destroyTransaction(newTransaction);
		return returnCode;
	}
	if (updateTransaction(newTransaction, service->repository) == 0)
	{
		destroyTransaction(newTransaction);
		return 5;
	}
		
	return 0;
}


int deleteTransactionService(TransactionsService* service, int id)
{
	return deleteTransaction(id, service->repository);
}

int getTransactionsLength(TransactionsService* service)
{
	return getRepositoryLength(service->repository);
}

Transaction** copyList(Transaction** list, int length)
{
	if (length == 0)
		return NULL;

	Transaction** copy = malloc(length * sizeof(Transaction));
	if (copy != NULL)
	{
		for (int i = 0; i < length; i++)
		{
			Transaction* transaction = createTransaction(list[i]->id, list[i]->day, list[i]->sum, list[i]->type, list[i]->description);
			if (transaction != NULL)
				copy[i] = transaction;
		}
	}
	return copy;

}

Transaction** getTransactionsService(int* length, TransactionsService* service)
{
	*length = getRepositoryLength(service->repository);
	return getTransactions(service->repository);
}

Transaction** getTransactionsFilteredByType(int type, int* length, TransactionsService* service)
{
	Transaction** filteredList=getTransactionsService(length, service);
	filteredList = copyList(filteredList, *length);
	
	for (int i = 0; i < *length; i++)
	{
		if (filteredList[i]->type != type)
		{
			destroyTransaction(filteredList[i]);
			for (int j = i; j < *length - 1; j++)
				filteredList[j] = filteredList[j + 1];
			(*length)--;
			i--;
		}
	}
	if (*length == 0)
		return NULL;
	return filteredList;
}

Transaction** getTransactionsFilteredBySum(float sum, int* length, int property, TransactionsService* service)
{
	if (sum < 0)
	{
		return NULL;
	}

	Transaction** filteredList=getTransactionsService(length, service);
	filteredList = copyList(filteredList, *length);

	if (property)
	{
		for (int i = 0; i < *length; i++)
		{
			if (filteredList[i]->sum >= sum)
			{
				destroyTransaction(filteredList[i]);
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
			if (filteredList[i]->sum < sum)
			{
				destroyTransaction(filteredList[i]);
				for (int j = i; j < *length - 1; j++)
					filteredList[j] = filteredList[j + 1];
				(*length)--;
				i--;
			}
		}
	}

	if (*length == 0)
		return NULL;

	return filteredList;
}

int compareSum(Transaction* transaction1,Transaction* transaction2)
{
	return transaction1->sum > transaction2->sum;
}

int compareDay(Transaction* transaction1, Transaction* transaction2)
{
	return transaction1->day > transaction2->day;
}

Transaction** getTransactionsSortedBySum(int ascending, TransactionsService* service, int (*compareSum)(Transaction*, Transaction*))
{
	int length = 0;
	Transaction** sortedList=getTransactionsService(&length, service);
	sortedList = copyList(sortedList, length);

	if (length == 0)
	{
		return NULL;
	}

	Transaction* aux;

	if (ascending)
	{
		for (int i = 0; i < length; i++)
			for (int j = i + 1; j < length; j++)
			{
				if (compareSum(sortedList[i],sortedList[j]))
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
				if (!compareSum(sortedList[i], sortedList[j]))
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}

	return sortedList;
}

Transaction** getTransactionsSortedByDay(int ascending, TransactionsService* service, int (*compareDay)(Transaction*, Transaction*))
{
	int length = 0;
	Transaction** sortedList = getTransactionsService(&length, service);
	sortedList = copyList(sortedList, length);

	if (length == 0)
	{
		return NULL;
	}

	Transaction* aux;

	if (ascending)
	{
		for (int i = 0; i < length; i++)
			for (int j = i + 1; j < length; j++)
			{
				if (compareDay(sortedList[i], sortedList[j]))
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
				if (!compareDay(sortedList[i], sortedList[j]))
				{
					aux = sortedList[i];
					sortedList[i] = sortedList[j];
					sortedList[j] = aux;
				}
			}
	}

	return sortedList;
}

void destroyService(TransactionsService* service)
{
	destroyRepository(service->repository);
	free(service);
}
