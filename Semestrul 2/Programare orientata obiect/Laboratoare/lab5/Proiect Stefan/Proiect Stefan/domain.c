#define _CRT_SECURE_NO_WARNINGS
#include "domain.h"
#include <string.h>
#include <stdio.h>


materiePrima* creeazaMateriePrima(char* nume, char* producator, int cantitate)
{
	materiePrima* m = (materiePrima*)malloc(sizeof(materiePrima));
	if (m != NULL) {
		m->nume = (char*)malloc(sizeof(char) * (strlen(nume) + 1));
		m->producator = (char*)malloc(sizeof(char) * (strlen(producator) + 1));
		if (m->nume != NULL && m->producator != NULL) {
			strcpy(m->nume, nume);
			strcpy(m->producator, producator);
		}
		m->cantitate = cantitate;
	}
	return m;
}

void distrugeMateriePrima(materiePrima* m)
{
	free(m->nume);
	free(m->producator);
	free(m);
}

materiePrima* copyMateriePrima(materiePrima* m)
{
	/*materiePrima* copie = (materiePrima*)malloc(sizeof(materiePrima));
	if (copie != NULL) {
		copie->nume = (char*)malloc(sizeof(char) * (strlen(m->nume) + 1));
		copie->producator = (char*)malloc(sizeof(char) * (strlen(m->producator) + 1));
		if (copie->nume != NULL && copie->producator != NULL) {
			strcpy(copie->nume, m->nume);
			strcpy(copie->producator, m->producator);
		}
		copie->cantitate = m->cantitate;
	}
	return copie;*/
	return creeazaMateriePrima(m->nume, m->producator, m->cantitate);
}
