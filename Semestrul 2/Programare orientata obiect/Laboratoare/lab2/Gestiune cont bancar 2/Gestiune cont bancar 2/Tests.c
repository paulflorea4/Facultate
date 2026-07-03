#include "Transaction.h"
#include <stdlib.h>
#include "Repository.h"
#include "Service.h"
#include "assert.h"
#include <string.h>

void runDomainTests()
{
    Transaction* transaction1 = createTransaction(1, 12, 300, 1, "descriere");
    assert(validateTransaction(*transaction1) == 0);
    printTransactionStringFormat(transaction1);
    destroyTransaction(transaction1);

    Transaction* transaction2 = createTransaction(-1, 10, 500, 2, "descriere2");
    assert(validateTransaction(*transaction2) == 1);
    destroyTransaction(transaction2);

    Transaction* transaction3 = createTransaction(3, 32, 1000, 2, "");
    assert(validateTransaction(*transaction3) == 2);
    destroyTransaction(transaction3);

    Transaction* transaction4 = createTransaction(4, 10, 100.0, 1, "");
    assert(strcmp(getTypeString(transaction4), "Intrare") == 0);
    destroyTransaction(transaction4);

    Transaction* transaction5 = createTransaction(5, 10, 100.0, 2, "");
    assert(strcmp(getTypeString(transaction5), "Iesire") == 0);
    destroyTransaction(transaction5);

    Transaction* transaction6 = createTransaction(6, 10, 100.0, 3, "");
    assert(strcmp(getTypeString(transaction6), "Invalid") == 0);
    destroyTransaction(transaction6);
}


void runRepositoryTests()
{
    TransactionsRepository* repository = createTransactionsRepository();
    assert(getRepositoryLength(repository) == 0);

    Transaction** list = getTransactions(repository);
    assert(list == NULL);

    Transaction* transaction1 = createTransaction(1, 12, 300, 1, "descriere");
    Transaction* transaction2 = createTransaction(2, 12, 1000, 1, "descriere");
    Transaction* transaction3 = createTransaction(3, 14, 500, 2, "");

    assert(addTransaction(transaction1, repository) == 1);
    assert(getRepositoryLength(repository) == 1);

    assert(addTransaction(transaction1, repository) == 0); 
    assert(getRepositoryLength(repository) == 1);

    assert(addTransaction(transaction2, repository) == 1);
    assert(getRepositoryLength(repository) == 2);

    Transaction* newTransaction3 = createTransaction(3, 24, 400, 1, "");

    assert(updateTransaction(newTransaction3, repository) == 0); 
    assert(addTransaction(transaction3, repository) == 1);
    assert(getRepositoryLength(repository) == 3);

    assert(updateTransaction(newTransaction3, repository) == 1); 
    assert(getRepositoryLength(repository) == 3);

    assert(deleteTransaction(4, repository) == 0);

    assert(deleteTransaction(3, repository) == 1);
    assert(getRepositoryLength(repository) == 2);

    list = getTransactions(repository);
    assert(list != NULL);
    assert(list[0]->id == transaction1->id);
    assert(list[1]->id == transaction2->id);
    assert(getRepositoryLength(repository) == 2);

    destroyRepository(repository);
}

void runServiceTests()
{
    TransactionsService* service = createTransactionsService();
    assert(getTransactionsLength(service) == 0);

    Transaction** list;
    int length;

    assert(addTransactionService(service, -1, 20, 200, 1, "") == 1);
    assert(addTransactionService(service, 1, -2, 200, 1, "") == 2);
    assert(addTransactionService(service, 1, 20, -200, 1, "") == 3);
    assert(addTransactionService(service, 3, 20, 200, 4, "") == 4);
    assert(addTransactionService(service, 5, 20, 200, 1, "") == 0);
    assert(getTransactionsLength(service) == 1);

    assert(addTransactionService(service, 5, 24, 1000, 2, "") == 5);

    assert(updateTransactionService(service, 3, 19, 500, 2, "aaa") == 5);
    assert(updateTransactionService(service, 5, 34, 1500, 1, "") == 2);
    assert(updateTransactionService(service, 5, 15, 400, 2, "") == 0);

    assert(deleteTransactionService(service, 3) == 0);
    assert(deleteTransactionService(service, 5) == 1);
    assert(getTransactionsLength(service) == 0);

    list = getTransactionsFilteredByType(1, &length, service);
    assert(length == 0);
    free(list);

    list = getTransactionsFilteredByType(3, &length, service);
    assert(length == 0);
    free(list);

    list = getTransactionsFilteredBySum(500, &length, 1, service);
    assert(length == 0);
    free(list);

    list = getTransactionsFilteredBySum(-100, &length, 1, service);
    assert(length == 0);
    free(list);

    list = getTransactionsSortedByDay(1, service,compareDay);
    assert(list == NULL);

    list = getTransactionsSortedBySum(1, service,compareSum);
    assert(list == NULL);

    addTransactionService(service, 1, 10, 100.0, 1, "Test1");
    addTransactionService(service, 2, 15, 200.0, 2, "Test2");
    addTransactionService(service, 3, 20, 150.0, 1, "Test3");
    addTransactionService(service, 4, 25, 250.0, 2, "Test4");
    addTransactionService(service, 5, 5, 300.0, 1, "Test5");

    list = getTransactionsFilteredByType(1, &length, service);
    assert(length == 3);
    assert(list[0]->id == 1);
    assert(list[1]->id == 3);
    assert(list[2]->id == 5);
    for (int i = 0; i < length; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsFilteredByType(2, &length, service);
    assert(length == 2);
    assert(list[0]->id == 2);
    assert(list[1]->id == 4);
    for (int i = 0; i < length; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsFilteredBySum(200.0, &length, 1, service);
    assert(length == 2);
    assert(list[0]->id == 1);
    assert(list[1]->id == 3);
    for (int i = 0; i < length; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsFilteredBySum(200.0, &length, 0, service);
    assert(length == 3);
    assert(list[0]->id == 2);
    assert(list[1]->id == 4);
    assert(list[2]->id == 5);
    for (int i = 0; i < length; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsSortedBySum(1, service,compareSum);
    assert(list[0]->id == 1);
    assert(list[1]->id == 3);
    assert(list[2]->id == 2);
    assert(list[3]->id == 4);
    assert(list[4]->id == 5);
    for (int i = 0; i < 5; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsSortedBySum(0, service,compareSum);
    assert(list[0]->id == 5);
    assert(list[1]->id == 4);
    assert(list[2]->id == 2);
    assert(list[3]->id == 3);
    assert(list[4]->id == 1);
    for (int i = 0; i < 5; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsSortedByDay(1, service,compareDay);
    assert(list[0]->id == 5);
    assert(list[1]->id == 1);
    assert(list[2]->id == 2);
    assert(list[3]->id == 3);
    assert(list[4]->id == 4);
    for (int i = 0; i < 5; i++)
        destroyTransaction(list[i]);
    free(list);

    list = getTransactionsSortedByDay(0, service,compareDay);
    assert(list[0]->id == 4);
    assert(list[1]->id == 3);
    assert(list[2]->id == 2);
    assert(list[3]->id == 1);
    assert(list[4]->id == 5);
    for (int i = 0; i < 5; i++)
        destroyTransaction(list[i]);
    free(list);

    destroyService(service);
}


void runTests()
{
    runDomainTests();
    runRepositoryTests();
    runServiceTests();
    system("cls");
}



