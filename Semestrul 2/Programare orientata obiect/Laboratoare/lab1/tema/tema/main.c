#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

/*
* 5.Tipareste un numar precizat de termeni din sirul
   1, 2,1, 3,2,1, 4,2,2, 5,4,3,2,1, 6,2,2,3,3,3, 7,6, ...
   obtinut din sirul numerelor naturale prin inlocuirea fiecarui
   numar natural n printr-un grup de numere. Numarul prim p este
   inlocuit prin numerele p,p-1,...3,2,1, iar numarul compus n
   este inlocuit prin n urmat de toti divizorii sai proprii,
   un divizor d repetandu-se de d ori.
*/
void menu()
{
	/*
	* Afiseaza meniul utilizatorului
	* parametrii:-
	* return:-
	*/
	printf("1.Tipareste numar precizat de termeni.\n");
	printf("2.Iesire.\n");
}

bool prim(int x)
{
	/*
	* Verifica daca numarul dat ca parametru este prim
	* parametrii:x-numar intreg,pozitiv
	* return:True daca numarul e prim,False altfel
	*/
	if (x<=1)
		return false;
	if (x % 2 == 0 && x!=2)
		return false;
	for (int i = 3; i <= x/2; i=i+2)
		if (x % i == 0)
			return false;
	return true;
}

void print_terms()
{
	/*
	* Creeaza lista formata din primii n termeni ai sirului si ii afiseaza
	* parametrii:-
	* return:-
	*/
	int nrTerms;
	printf("Introduceti numarul de termeni:");
	scanf("%d", &nrTerms);

	int index=0,currentNumber=1;

	if (nrTerms <= 0)
		printf("Numarul de termeni trebuie sa fie strict pozitiv.");
		

	while (index < nrTerms)
	{
		if (prim(currentNumber))
		{
			for (int i = currentNumber; i > 0 && index < nrTerms; i--)
			{
				printf("%d ",i);
				index++;
			}
		}
		else
		{
			printf("%d ", currentNumber);
			index++;
			for (int d = 2; d < currentNumber && index < nrTerms; d++)
			{
				if (currentNumber % d == 0)
				{
					for (int j = 0; j < d && index < nrTerms; j++)
					{
						printf("%d ", d);
						index++;
					}
				}
			}
		}
		currentNumber++;
	}
	printf("\n\n");
}



int main()
{
	/*
	* Functia main care gestioneaza optiunea utlizatorului
	* parametrii:-
	* return: 0 - cand utilizatorul iese din aplicatie
	*/
	assert(prim(1) == false);
	assert(prim(3) == true);

	int option;
	while (true)
	{
		menu();
		printf("Introduceti optiunea:");
		scanf("%d", &option);
		if (option == 1)
			print_terms();
		else if (option == 2)
			break;
		else
			printf("Optiune invalida.\n");
	}
	return 0;
}
