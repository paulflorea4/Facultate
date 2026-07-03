//#define _CRT_SECURE_NO_WARNINGS
//#include "repository.h"
//#include "domain.h"
//#include <stdlib.h>
//#include <string.h>
//
//void addMateriePrima(repo* r, materiePrima* materie) {
//	add(r->materii, materie);
//}
//
//repo* creeazaRepo(functieCopiere functieCopiere, functieDistrugere functieDistrugere)
//{
//	repo* r = creeazaLista(functieCopiere, functieDistrugere);
//	return r;
//}
//
//
//void distrugeRepo(repo* r) {
//	distrugeLista(r->materii);
//	free(r);
//}
//
//int getLen(repo* r)
//{
//	return r->materii->len;
//}
//
//int modificaMaterie(repo* r, char* nume, char* producator, int cantitate)
//{
//	for (int i = 0; i < r->materii->len; i++) {
//		materiePrima* m = (materiePrima*)r->materii->elemente[i];
//		if (strcmp(m->nume,nume)==0) {
//			free(m->producator);
//			m->producator = (char*)malloc(sizeof(char) * (strlen(producator) + 1));
//				if(m->producator!=NULL)
//			strcpy(m->producator, producator);
//			m->cantitate = cantitate;
//			return 1;
//		}
//	}
//	return 0;
//}
//
//int stergeMaterie(repo* r, char* nume)
//{
//	for (int i = 0; i < r->materii->len; i++) {
//		materiePrima* m = (materiePrima*)r->materii->elemente[i];
//		if (strcmp(m->nume, nume) == 0) {
//			distrugeMateriePrima(r->materii->elemente[i]);
//			for (int j = i; j < r->materii->len - 1; j++) {
//				r->materii[j] = r->materii[j + 1];
//			}
//			r->materii->len--;
//			return 1;
//		}
//	}
//	return 0;
//}
//
//int verificaExistenta(repo* r, char* nume)
//{
//	for (int i = 0; i < r->materii->len; i++) {
//		materiePrima* m = (materiePrima*) r->materii->elemente[i];
//		if (strcmp(m->nume, nume) == 0)
//			return 1;
//	}
//	return 0;
//}
//
//repo* copiazaRepo(repo* lista)
//{
//	repo* listaNoua = creeazaRepo(lista->materii->functieCopiere, lista->materi->functieDistrugere);
//	listaNoua->materii = copiazaLista(lista->materii);
//	return listaNoua;
//}
//
//TElem pop(repo* r)
//{
//	if (r->materii->len == 0)
//		return NULL;
//	TElem elem = r->materii[r->materii->len - 1];
//	r->len--;
//	return elem;
//}
    