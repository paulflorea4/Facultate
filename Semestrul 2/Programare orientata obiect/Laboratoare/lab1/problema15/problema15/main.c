#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>
/*
15. Determina primele n perechi de numere prime gemene, unde n este un
numar natural nenul dat.Doua numere prime p si q sunt gemene
daca q - p = 2.
*/
void menu()
{
	/*
	* Afiseaza meniul utilizatorului
	* parametrii:-
	* return:-
	*/
	printf("1.Tipareste primele n perechi de numere prime gemene.\n");
	printf("2.Iesire aplicatie.\n");
}

bool prim(int x)
{
	/*
	* Verifica daca numarul dat ca parametru este prim
	* parametrii:x-numar intreg,pozitiv
	* return:True daca numarul e prim,False altfel
	*/
	if (x <= 1)
		return false;
	if (x % 2 == 0 && x != 2)
		return false;
	for (int i = 3; i <= x / 2; i = i + 2)
		if (x % i == 0)
			return false;
	return true;
}

void print_pairs(int nrPairs)
{
	/*
	* Afiseaza primele n perechi de numere prime gemene
	* parametrii:nrPairs-numar intreg 
	* return:-
	*/
	if (nrPairs <= 0)
		printf("Numarul de perechi trebuie sa fie strict pozitiv.");
	int p = 2;
	while (nrPairs > 0)
	{
		if (prim(p) && prim(p + 2))
		{
			printf("(%d,%d) ", p, p + 2);
			nrPairs--;
		}
		p++;	
	}
	printf("\n\n");
}

int main() {
	/*
	* Functia main care gestioneaza optiunea utlizatorului
	* parametrii:-
	* return: 0 - cand utilizatorul iese din aplicatie
	*/

	assert(prim(1) == false);
	assert(prim(3) == true);
	assert(prim(11) == true);

	int option;
	while (true)
	{
		menu();
		printf("Introduceti optiunea:");
		scanf("%d", &option);
		if (option == 1)
		{
			int nrPairs;
			printf("Introduceti numarul de perechi:");
			scanf("%d", &nrPairs);
			print_pairs(nrPairs);
		}
		else if (option == 2)
			break;
		else
			printf("Optiune invalida.\n");
	}
	return 0;
}