#include "Repository.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

TransactionsRepository createTransactionsRepository()
{
	TransactionsRepository repository;
	repository.length = 0;
	return repository;
}

int addTransaction(Transaction transaction, TransactionsRepository* repository)
{
	int found = 0;
	for (int i = 0; i < repository->length; i++)
	{
		if (transaction.id == repository->transactions[i].id)
			found = 1;
	}
	if (!found)
	{
		repository->transactions[repository->length] = transaction;
		repository->length++;
		return 1;
	}
	return 0;
}

int updateTransaction(Transaction newTransaction, TransactionsRepository* repository)
{
	int index = -1;
	for (int i = 0; i < repository->length && index==-1; i++)
	{
		if (repository->transactions[i].id == newTransaction.id)
			index = i;
	}
	if (index!=-1)
	{
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
		if (repository->transactions[i].id == id)
			index = i;
	}
	if (index != -1)
	{	
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

void getTransactions(TransactionsRepository* repository,Transaction* list)
{
	for (int i = 0; i < repository->length; i++)
		list[i] = repository->transactions[i];
}
