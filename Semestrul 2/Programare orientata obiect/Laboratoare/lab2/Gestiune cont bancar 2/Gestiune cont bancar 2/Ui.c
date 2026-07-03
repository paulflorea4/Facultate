#define _CRT_SECURE_NO_WARNINGS
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "Service.h"

void printMenu()
{
	printf("Gestiune cond bancar\n");
	printf("1.Adauga o tranzactie\n");
	printf("2.Modifica o tranzactie\n");
	printf("3.Sterge o tranzactie\n");
	printf("4.Afiseaza lista de tranzactii\n");
	printf("5.Afiseaza lista de tranzactii filtrata\n");
	printf("6.Afiseaza lista de tranzactii ordonata\n");
	printf("7.Iesire\n");
	printf("Introduceti optiunea:");
}

int getIntegerFromUser()
{
	char input[100] = "";
	if (scanf("%s", input) == 0)
		return -1;

	char* endptr;
	long value = strtol(input, &endptr, 10);
	if (*endptr != '\0' && *endptr != '\n')
		return -1;

	if (value < 0)
		return -1;

	return value;
}

float getFloatFromUser()
{
	char input[100] = "";
	if (scanf("%s", input) == 0)
		return -1;

	char* endptr;
	float value = strtof(input, &endptr);
	if (*endptr != '\0' && *endptr != '\n')
		return -1;

	if (value < 0)
		return -1;

	return value;
}


bool readIDFromUser(int* id)
{
	printf("Introduceti id-ul tranzactiei:");
	*id = getIntegerFromUser();
	if (*id == -1)
		printf("Id-ul trebuie sa fie un numar natural!\n");
	return *id != -1;
}

bool readDayFromUser(int* day)
{
	printf("Introduceti ziua in care a fost realizata tranzactia:");
	*day = getIntegerFromUser();
	if (*day == -1)
		printf("Ziua trebuie sa fie un numar natural!\n");
	return *day != -1;
}

bool readSumFromUser(float* sum)
{
	printf("Introduceti suma tranzactiei:");
	*sum = getFloatFromUser();
	if (*sum == -1)
		printf("Suma trebuie sa fie un numar real!\n");
	return *sum != -1;
}

bool readTypeFromUser(int* type)
{
	printf("Introduceti tipul tranzactiei:\n");
	printf("  1.Intrare\n");
	printf("  2.Iesire\n");
	printf("Introduceti optiunea:");
	*type = getIntegerFromUser();
	if (*type == -1)
		printf("Tipul trebuie sa fie un numar natural!\n");
	return *type != -1;
}

void readDescriptionFromUser(char* description)
{
	int c;
	while ((c = getchar()) != '\n' && c != EOF);

	printf("Introduceti descrierea tranzactiei(max. 100 caractere): ");

	fgets(description, 100, stdin);
}

void addTransactionUI(TransactionsService* service)
{
	int id, day, type;
	float sum;
	char* description=malloc(100*sizeof(char));

	if (readIDFromUser(&id) == false)
		return;
	if (readDayFromUser(&day) == false)
		return;
	if (readSumFromUser(&sum) == false)
		return;
	if (readTypeFromUser(&type) == false)
		return;
	readDescriptionFromUser(description);

	int errorCode = addTransactionService(service, id, day, sum, type, description);
	switch (errorCode) {
	case 0:
		printf("Tranzactia a fost adaugata cu succes!\n");
		return;
	case 1:
		printf("Id-ul este invalid!\n");
		return;
	case 2:
		printf("Ziua este invalida!\n");
		return;
	case 3:
		printf("Suma este invalida!\n");
		return;
	case 4:
		printf("Tipul este invalida!\n");
		return;
	case 5:
		printf("Exista deja o tranzactie cu id-ul %d!\n", id);
		return;
	default:
		break;
	}
	free(description);
}

void updateTransactionUI(TransactionsService* service)
{
	int id, day, type;
	float sum;
	char description[100];

	if (readIDFromUser(&id) == false)
		return;
	if (readDayFromUser(&day) == false)
		return;
	if (readSumFromUser(&sum) == false)
		return;
	if (readTypeFromUser(&type) == false)
		return;
	readDescriptionFromUser(description);

	int errorCode = updateTransactionService(service, id, day, sum, type, description);
	switch (errorCode) {
	case 0:
		printf("Tranzactia a fost modificata cu succes!\n");
		return;
	case 1:
		printf("Id-ul este invalid!\n");
		return;
	case 2:
		printf("Ziua este invalida!\n");
		return;
	case 3:
		printf("Suma este invalida!\n");
		return;
	case 4:
		printf("Tipul este invalid!\n");
		return;
	case 5:
		printf("Nu exista o tranzactie cu id-ul %d!\n", id);
		return;
	default:
		break;
	}
}

void deleteTransactionUI(TransactionsService* service)
{
	int id;
	if (readIDFromUser(&id) == false)
		return;
	int deleted = deleteTransactionService(service, id);
	if (deleted == 1)
		printf("Tranzactia a fost stearsa cu succes!\n");
	else
		printf("Nu exista o tranzactie cu id-ul %d!\n", id);
}

void printTransactions(TransactionsService* service)
{
	int length;
	Transaction** transactions;
	transactions=getTransactionsService(&length, service);
	if (length != 0)
	{
		for (int i = 0; i < length; i++)
		{
			printTransactionStringFormat(transactions[i]);

		}
	}
	else
		printf("Nu exista tranzactii!\n");
	printf("\n");
}

void printFilteredTransactions(TransactionsService* service)
{
	printf("Filtrare dupa:\n");
	printf("  1.Tip\n");
	printf("  2.Suma\n");
	printf("Introduceti optiunea:");
	int filterType = getIntegerFromUser();

	int type, length;
	float sum;
	Transaction** filteredList=NULL;

	switch (filterType)
	{
	case 1:
		if (readTypeFromUser(&type) == false)
			return;
		if (type != 1 && type != 2)
		{
			printf("Optiune invalida!\n");
			return;
		}
		else
			filteredList=getTransactionsFilteredByType(type, &length, service);
		break;
	case 2:
		if (readSumFromUser(&sum) == false)
			return;
		printf("  1.Tranzactiile mai mici decat %.2f\n", sum);
		printf("  2.Tranzactiile mai mari decat %.2f\n", sum);
		printf("Introduceti optiunea:");
		int sumProperty = getIntegerFromUser();
		if (sumProperty != 1 && sumProperty != 2)
		{
			printf("Optiune invalida!\n");
			return;
		}
		else
			filteredList=getTransactionsFilteredBySum(sum, &length, sumProperty % 2, service);
		break;
	default:
		printf("Optiune invalida!\n");
		return;
	}
	if (filteredList)
	{
		for (int i = 0; i < length; i++)
		{
			printTransactionStringFormat(filteredList[i]);
			destroyTransaction(filteredList[i]);
		}
		printf("\n");
	}
	else 
		printf("Nu exista astfel de tranzactii!\n");
	
}

void printSortedTransactions(TransactionsService* service)
{
	printf("Ordonare dupa:\n");
	printf("  1.Suma\n");
	printf("  2.Zi\n");
	printf("Introduceti optiunea:");
	int sortProperty = getIntegerFromUser();
	int sortType;
	Transaction** sortedList;
	switch (sortProperty)
	{
	case 1:
		printf("Alegeti modul de ordonare:\n");
		printf("  1.Crescator\n");
		printf("  2.Descrescator\n");
		printf("Introduceti optiunea:");
		sortType = getIntegerFromUser();
		if (sortType != 1 && sortType != 2)
		{
			printf("Optiune invalida!\n");
			return;
		}
		else
			sortedList=getTransactionsSortedBySum(sortType % 2, service,compareSum);
		break;
	case 2:
		printf("Alegeti modul de ordonare:\n");
		printf("  1.Crescator\n");
		printf("  2.Descrescator\n");
		printf("Introduceti optiunea:");
		sortType = getIntegerFromUser();
		if (sortType != 1 && sortType != 2)
		{
			printf("Optiune invalida!\n");
			return;
		}
		else
			sortedList = getTransactionsSortedByDay(sortType % 2, service,compareDay);
		break;
	default:
		printf("Optiunea invalida!\n");
		return;
	}
	if (sortedList)
	{
		int length = getTransactionsLength(service);
		for (int i = 0; i < length; i++)
		{
			printTransactionStringFormat(sortedList[i]);
			destroyTransaction(sortedList[i]);
		}
		printf("\n");
	}
	else 
		printf("Nu exista tranzactii!\n");
}

void handleInput(int option, TransactionsService* service)
{
	switch (option) {
	case 1:
		addTransactionUI(service);
		break;
	case 2:
		updateTransactionUI(service);
		break;
	case 3:
		deleteTransactionUI(service);
		break;
	case 4:
		printTransactions(service);
		break;
	case 5:
		printFilteredTransactions(service);
		break;
	case 6:
		printSortedTransactions(service);
		break;
	case 7:
		exit(0);
	default:
		printf("Optiune invalida!\n");
	}
	printf("\n");
}

void runApp()
{
	TransactionsService* service = createTransactionsService();
	while (true)
	{
		printMenu();
		int option = getIntegerFromUser();
		handleInput(option, service);
	}
	destroyService(service);
}