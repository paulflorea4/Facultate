#define _CRT_SECURE_NO_WARNINGS
#include <stdlib.h>
#include "list.h"
#include "domain.h"
#include <string.h>

#define INIT_CAPACITY 2

List* creeazaLista(functieCopiere functieCopiere, functieDistrugere functieDistrugere)
{
	List* list = malloc(sizeof(List));
	if (list != NULL)
	{
		list->len = 0;
		list->capacitate = INIT_CAPACITY;
		list->elemente = malloc(INIT_CAPACITY * sizeof(TElem));
		list->functieCopiere = functieCopiere;
		list->functieDistrugere = functieDistrugere;
	}
	return list;
}

void distrugeLista(List* l)
{
	for (int i = 0; i < l->len; i++)
	{
		l->functieDistrugere(l->elemente[i]);
	}
	free(l->elemente);
	free(l);
}

List* copiazaLista(List* l)
{
	List* copy = creeazaLista(l->functieCopiere, l->functieDistrugere);
	for (int i = 0; i < l->len; i++)
	{
		add(copy, l->functieCopiere(l->elemente[i]));
	}
	return copy;
}

int getLen(List* l)
{
	return l->len;
}

void add(List* l, TElem element)
{
	if (l->capacitate == l->len) {
		l->capacitate *= 2;
		TElem* listaNoua = malloc(sizeof(TElem) * l->capacitate);
		if (listaNoua != NULL)
		{
			for (int i = 0; i < l->len; i++)
				listaNoua[i] = l->elemente[i];
		}
		free(l->elemente);
		l->elemente = listaNoua;
	}
	if (l->elemente != NULL)
		l->elemente[l->len++] = element;
}

int updateMaterie(List* l, TElem elementNou)
{
	for (int i = 0; i < l->len; i++)
	{
		materiePrima* materie = (materiePrima*)l->elemente[i];
		if (strcmp(materie->nume, ((materiePrima*)elementNou)->nume) == 0)
		{
			free(materie->producator);
			materie->producator = (char*)malloc(sizeof(char) * (strlen(((materiePrima*)elementNou)->producator) + 1));
			if(materie->producator!=NULL)
				strcpy(materie->producator, ((materiePrima*)elementNou)->producator);
			materie->cantitate = ((materiePrima*)elementNou)->cantitate;
			return 1;
		}
	}
	return 0;
}

int stergeMaterie(List* l, char* nume)
{
	for (int i = 0; i < l->len; i++) {
		materiePrima* materie = (materiePrima*)l->elemente[i];
		if (strcmp(materie->nume, nume) == 0) {
			distrugeMateriePrima(l->elemente[i]);
			for (int j = i; j < l->len - 1; j++) {
				l->elemente[j] = l->elemente[j + 1];
			}
			l->len--;
			return 1;
		}
	}
	return 0;
}

int verificaExistenta(List* l, char* nume)
{
	for (int i = 0; i < l->len; i++) {
		materiePrima* m = (materiePrima*) l->elemente[i];
		if (strcmp(m->nume, nume) == 0)
			return 1;
	}
	return 0;
}

TElem getMateriePrimaRepo(List* l, int poz)
{
	if (poz < 0 || poz >= l->len)
		return 0;
	return l->elemente[poz];
}

TElem pop(List* l)
{
	TElem element = l->elemente[l->len - 1];
	l->len -= 1;
	return element;
}

materiePrima* getMaterieDupaNume(List* l, char* nume)
{
	for (int i = 0; i < l->len; i++)
	{
		materiePrima* m = (materiePrima*)getMateriePrimaRepo(l, i);
		if (strcmp(m->nume, nume) == 0)
			return m;
	}
	return NULL;
}

