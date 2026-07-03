#include "service.h"
#include <string.h>

int valideazaMateriePrima(materiePrima* m)
{
	if (m->cantitate < 0)
		return 0;
	if (strlen(m->nume) == 0)
		return 0;
	if (strlen(m->producator) == 0)
		return 0;
	return 1;
}

int addMaterieService(service* s, char* nume, char* producator, int cantitate)
{
	materiePrima* m = creeazaMateriePrima(nume, producator, cantitate);
	int ok = valideazaMateriePrima(m);
	if (ok == 1)
	{
		if (verificaExistenta(s->repository, m->nume) == 0) {
			List* copy = copiazaLista(s->repository);
			add(s->undoList, copy);
			add(s->repository, m);
			return 1;
		}
	}
	distrugeMateriePrima(m);
	return 0;
}


materiePrima* getMateriePrima(service* s, int poz)
{
	return getMateriePrimaRepo(s->repository, poz);
}

int modificaMaterieService(service* s, char* nume, char* producator, int cantitate)
{
	materiePrima* m = creeazaMateriePrima(nume, producator, cantitate);
	int ok = valideazaMateriePrima(m);
	if (ok == 1) {
		if (verificaExistenta(s->repository, m->nume) == 1) {
			List* copy = copiazaLista(s->repository);
			add(s->undoList, copy);

			updateMaterie(s->repository, m);
			distrugeMateriePrima(m);
			return 1;
		}
	}
	distrugeMateriePrima(m);
	return 0;
}

int stergeMaterieService(service* s, char* nume)
{
	if (verificaExistenta(s->repository, nume) == 1) {
		List* copy = copiazaLista(s->repository);
		add(s->undoList, copy);

		add(s->deletedList, copyMateriePrima(getMaterieDupaNume(s->repository,nume)));
		stergeMaterie(s->repository, nume);
		return 1;
	}
	return 0;
}

service* creeazaRepoService()
{
	service* s = malloc(sizeof(service));
	if (s != NULL) {
		s->repository = creeazaLista(copyMateriePrima, distrugeMateriePrima);
		s->undoList = creeazaLista(copiazaLista, distrugeLista);
		s->deletedList = creeazaLista(copyMateriePrima, distrugeMateriePrima);
	}
	return s;
}

List* filtrareDupaLitera(service* sursa, char litera)
{
	List* rez = creeazaLista(copyMateriePrima, distrugeMateriePrima);
	int len = getLen(sursa->repository);
	for (int i = 0; i < len; i++) {
		materiePrima* m = getMateriePrima(sursa, i);
		if (m->nume[0] == litera) {
			materiePrima* copie = copyMateriePrima(m);
			add(rez, copie);
		}
	}
	return rez;
}

List* filtrareDupaProducator(service* sursa, char* producator)
{
	List* rez = creeazaLista(copyMateriePrima, distrugeMateriePrima);
	int len = getLen(sursa->repository);
	for (int i = 0; i < len; i++)
	{
		materiePrima* m = getMateriePrima(sursa, i);
		if (strcmp(m->producator,producator)==0) {
			materiePrima* copie = copyMateriePrima(m);
			add(rez, copie);
		}
	}
	return rez;
}

List* filtrareDupaCantitate(service* sursa, int cantitate)
{
	List* rez = creeazaLista(copyMateriePrima, distrugeMateriePrima);;
	int len = getLen(sursa->repository);
	for (int i = 0; i < len; i++) {
		materiePrima* m = getMateriePrima(sursa, i);
		if (m->cantitate < cantitate) {
			materiePrima* copie = copyMateriePrima(m);
			add(rez, copie);
		}
	}
	return rez;
}

void generareMateriiPrime(service* s)
{
	addMaterieService(s, "faina", "Molino SpA", 100);
	addMaterieService(s, "zahar", "SweetCo", 200);
	addMaterieService(s, "unt", "DairyBest", 150);
	addMaterieService(s, "oua", "FarmFresh", 300);
	addMaterieService(s, "lapte", "MilkLand", 250);
	addMaterieService(s, "cacao", "ChocoDelight", 80);
	addMaterieService(s, "ulei", "OilPure", 120);
	addMaterieService(s, "drojdie", "YeastMaster", 60);
	addMaterieService(s, "sare", "SaltWorks", 90);
	addMaterieService(s, "vanilie", "VanillaEssence", 70);
}

int cmpNume(materiePrima* m1, materiePrima* m2)
{
	if (strcmp(m1->nume, m2->nume) > 0)
		return 1;
	if (strcmp(m1->nume, m2->nume) < 0)
		return -1;
	return 0;
}

int cmpNumeDesc(materiePrima* m1, materiePrima* m2)
{
	if (strcmp(m1->nume, m2->nume) > 0)
		return -1;
	if (strcmp(m1->nume, m2->nume) < 0)
		return 1;
	return 0;
}

int cmpCantitate(materiePrima* m1, materiePrima* m2)
{
	if (m1->cantitate > m2->cantitate)
		return 1;
	if (m1->cantitate < m2->cantitate)
		return -1;
	return 0;
}

int cmpCantitateDesc(materiePrima* m1, materiePrima* m2)
{
	if (m1->cantitate > m2->cantitate)
		return -1;
	if (m1->cantitate < m2->cantitate)
		return 1;
	return 0;
}

void sortare(service* s, int(*cmp)(materiePrima*, materiePrima*)) {
	int leng = getLen(s->repository);
	for (int i = 0; i < leng - 1; i++) {
		for (int j = i + 1; j < leng; j++) {
			materiePrima* m1 = getMateriePrima(s, i);
			materiePrima* m2 = getMateriePrima(s, j);
			if (cmp(m1, m2) > 0) {
				materiePrima temp = *m1;
				*m1 = *m2;
				*m2 = temp;
			}
		}
	}
}

int undo(service* s)
{
	if (getLen(s->undoList) == 0)
		return 1;
	List* l = pop(s->undoList);
	distrugeLista(s->repository);
	s->repository = l;
	return 0;
}

void distrugeService(service* s)
{
	distrugeLista(s->repository);
	distrugeLista(s->undoList);
	distrugeLista(s->deletedList);
	free(s);
}

