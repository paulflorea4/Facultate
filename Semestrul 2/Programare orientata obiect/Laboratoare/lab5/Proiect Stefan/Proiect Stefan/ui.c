#define _CRT_SECURE_NO_WARNINGS
#include "ui.h"
#include <stdlib.h>
#include <stdio.h>
#include "service.h"
#include "domain.h"
#include "tests.h"

void printMenu()
{
	printf("\n");
	printf("1. Adauga materie prima\n");
	printf("2. Modifica materie prima\n");
	printf("3. Sterge materie prima\n");
	printf("4. Filtrare\n");
	printf("5. Sortare\n");
	printf("6. Afisare materii prime\n");
	printf("7. Generare materii prime\n");
	printf("8. Undo\n");
	printf("9. Afisare lista de materii sterse\n");
	printf("x. Iesire\n");
	printf("\n");
}

void run()
{
	service* s = creeazaRepoService();
	int Running = 1;
	while (Running) {
		char optiune;
		printMenu();
		printf(">>> ");
		if(scanf(" %c", &optiune)){}
		switch (optiune) {
		case '1': {
			//adauga
			int cantitate;
			char nume[20], producator[20];
			printf("Introdu nume: ");
			if(scanf(" %s", nume)){}
			printf("Introdu producator: ");
			if(scanf(" %s", producator)){}
			printf("Introdu cantitate: ");
			if(scanf(" %d", &cantitate)){}
			int ok = addMaterieService(s, nume, producator, cantitate);
			if (ok) {
				printf("Adaugare efectuata cu succes\n");
			}
			else {
				printf("Adaugare esuata\n");
			}
			break;
		}
		case '2': {
			//modifica
			char nume[20];
			char producator[20];
			int cantitate;
			printf("Introdu nume: ");
			if(scanf(" %s", nume)){}
			printf("Introdu producator: ");
			if(scanf(" %s", producator)){}
			printf("Introdu cantitate: ");
			if(scanf(" %d", &cantitate)){}
			int ok = modificaMaterieService(s, nume, producator, cantitate);
			if (ok) {
				printf("Modificare efectuata cu succes\n");
			}
			else {
				printf("Modificare esuata\n");
			}
			break;
		}
		case '3': {
			//stergere
			char nume[20];
			printf("Introdu nume materiei pe care vrei sa o stergi: ");
			if(scanf(" %s", &nume)){}
			int ok = stergeMaterieService(s, nume);
			if (ok) {
				printf("Stergere efectuata cu succes\n");
			}
			else {
				printf("Stergere esuata\n");
			}
			break;
		}
		case '4': {
			//filtrare
			int isRunning = 1;
			while (isRunning) {
				printMenuFiltrare();
				char optiuneFiltrare;
				printf(">>> ");
				if(scanf(" %c", &optiuneFiltrare)){}
				switch (optiuneFiltrare) {
				case 'a': {
					//filtrare dupa nume
					char litera;
					printf("Introdu litera: ");
					if(scanf(" %c", &litera)){}
					
					List* rezultate = filtrareDupaLitera(s, litera);
					printf("\nFitrare dupa litera %c :\n", litera);
					printf("\n");
					afisareRepo(rezultate);
					printf("\n");
					distrugeLista(rezultate);
					break;
				}
				case 'b': {
					//filtrare dupa cantitate
					int cantitate;
					printf("Introdu cantitatea: ");
					if(scanf(" %d", &cantitate)){}
					
					List* rezultate = filtrareDupaCantitate(s, cantitate);
					printf("\nFiltrare dupa cantitatea %d :\n", cantitate);
					printf("\n");
					afisareRepo(rezultate);
					printf("\n");
					distrugeLista(rezultate);
					break;
				}
				case 'c': {
					//filtrare dupa producator
					char producator[20];
					printf("Introdu producator: ");
					if (scanf(" %s", producator)) {}
					List* rezultate = filtrareDupaProducator(s, producator);
					printf("\nFiltrare dupa producatorul %s :\n", producator);
					printf("\n");
					afisareRepo(rezultate);
					printf("\n");
					distrugeLista(rezultate);
					break;
				}
				case 'x': {
					isRunning = 0;
					break;
				}
				default: {
					printf("Optiune invalida\n");
					break;
				}
				}
			}
			break;
		}
		case '5': {
			//sortare
			int isRunning = 1;
			while (isRunning) {
				printMenuSortare();
				char optiuneSortare;
				printf(">>> ");
				if(scanf(" %c", &optiuneSortare)){}
				switch (optiuneSortare) {
				case 'a': {
					//sortare dupa nume crescator
					sortare(s, cmpNume);
					printf("\n");
					printf("\nSortare dupa nume crescator:\n");
					afisareRepo(s->repository);
					printf("\n");
					break;
				}
				case 'b': {
					//sortare dupa nume descrescator
					sortare(s, cmpNumeDesc);
					printf("\n");
					printf("\nSortare dupa nume descrescator:\n");
					afisareRepo(s->repository);
					printf("\n");
					break;
				}
				case 'c': {
					// sortare dupa cantitate crescator
					sortare(s, cmpCantitate);
					printf("\n");
					printf("\nSortare dupa nume crescator:\n");
					afisareRepo(s->repository);
					printf("\n");
					break;
				}
				case 'd': {
					//sortare dupa cantitate descrescator
					sortare(s, cmpCantitateDesc);
					printf("\n");
					printf("\nSortare dupa nume descrescator:\n");
					afisareRepo(s->repository);
					printf("\n");
					break;
				}
				case 'x': {
					isRunning = 0;
					break;
				}
				default: {
					printf("Optiune invalida\n");
					break;
				}
				}
			}
			break;
		}
		case '6': {
			// afisare
			afisareRepo(s->repository);
			break;
		}
		case '7': {
			// generare materii prime
			generareMateriiPrime(s);
			printf("Materii prime generate cu succes\n");
			printf("\n");
			afisareRepo(s->repository);
			break;
		}
		case '8': {
			//undo
			if (undo(s) == 0)
				printf("Undo realizat cu succes\n");
			else
				printf("Nu se mai poate face undo\n");
			break;
		}
		case '9': {
			//lista materii sterse
			afisareRepo(s->deletedList);
			break;
		}
		case 'x':
			distrugeService(s);
			Running = 0;
			break;
		default:
			printf("Optiune invalida\n");
			break;
		}
	}
	
}

void printMenuFiltrare()
{
	printf("a. Afisare materii prime al caror nume incep cu o litera data\n");
	printf("b. Afisare materii prime care au cantitatea mai mica decat o cantitate data\n");
	printf("c. Afisare materii prime care au un producator dat\n");
	printf("x. Iesire\n");
	printf("\n");
}

void printMenuSortare()
{
	printf("a. Sortare dupa nume crescator\n");
	printf("b. Sortare dupa nume descrescator\n");
	printf("c. Sortare dupa cantitate crescator\n");
	printf("d. Sortare dupa cantitate descrescator\n");
	printf("x. Iesire\n");
	printf("\n");
}

void afisareRepo(List* l)
{
	int lungime = getLen(l);
	for (int i = 0; i < lungime; i++)
	{
		materiePrima* m = getMateriePrimaRepo(l, i);
		printf("Nume: %s, Producator: %s, Cantitate: %d\n", m->nume, m->producator, m->cantitate);
	}
}
