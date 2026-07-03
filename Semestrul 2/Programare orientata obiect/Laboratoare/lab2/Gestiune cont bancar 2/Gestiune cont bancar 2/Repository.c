#include "Repository.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

TransactionsRepository* createTransactionsRepository()
{
	TransactionsRepository* repository=(TransactionsRepository*)malloc(sizeof(TransactionsRepository));
	if (repository != NULL)
	{
		repository->capacity = 4;
		repository->length = 0;
		repository->transactions = malloc(repository->capacity * sizeof(Transaction*));
	}
	return repository;
}

int addTransaction(Transaction* transaction, TransactionsRepository* repository)
{
	for (int i = 0; i < repository->length; i++)
	{
		if (transaction->id == repository->transactions[i]->id)
			return 0;
	}

	if (repository->length >= repository->capacity)
	{
		int newCapacity = repository->capacity * 2;
		Transaction** newList = realloc( repository->transactions, sizeof(Transaction*) * newCapacity);
		if (newList != NULL)
		{
			repository->transactions = newList;
			repository->capacity = newCapacity;
		}
	}
	repository->transactions[repository->length] = transaction;
	repository->length++;
	return 1;
}

int updateTransaction(Transaction* newTransaction, TransactionsRepository* repository)
{
	int index = -1;
	for (int i = 0; i < repository->length && index == -1; i++)
	{
		if (repository->transactions[i]->id == newTransaction->id)
			index = i;
	}
	if (index != -1)
	{
		destroyTransaction(repository->transactions[index]);
		repository->transactions[index] = newTransaction;
		return 1;
	}
	return 0;
}

int deleteTransaction(int id, TransactionsRepository* repository)
{
	int index = -1;
	for (int i = 0; i < repository->length; i++)
	{
		if (repository->transactions[i]->id == id)
			index = i;
	}
	if (index != -1)
	{
		destroyTransaction(repository->transactions[index]);
		for (int i = index; i < repository->length; i++)
			repository->transactions[i] = repository->transactions[i + 1];
		repository->length--;
		return 1;
	}
	return 0;
}

int getRepositoryLength(TransactionsRepository* repository)
{
	return repository->length;
}

Transaction** getTransactions(TransactionsRepository* repository)
{
	if (repository->length == 0)
		return NULL;
	return repository->transactions;
}

void destroyRepository(TransactionsRepository* repository)
{
	for (int i = 0; i < repository->length; i++) {
		destroyTransaction(repository->transactions[i]);
	}
	free(repository->transactions);
	free(repository);
}