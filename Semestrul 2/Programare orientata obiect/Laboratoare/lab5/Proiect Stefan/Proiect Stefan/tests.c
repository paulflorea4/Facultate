#include "tests.h"
#include "domain.h"
#include <string.h>
#include <assert.h>
#include <stdio.h>
#include "repository.h"
#include "service.h"

void testCreeazaMateriePrima() {
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    assert(strcmp(m->nume, "nume") == 0);
    assert(strcmp(m->producator, "producator") == 0);
    assert(m->cantitate == 10);
    distrugeMateriePrima(m);
}

void testCreeazaRepoService() {
	service* s = creeazaRepoService();
	assert(s->repository->capacitate == 2);
	assert(s->repository->len == 0);
	assert(s->repository->elemente != NULL);
    distrugeService(s);
}

void testCopyMateriePrima() {
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    materiePrima* copie = copyMateriePrima(m);
    assert(strcmp(m->nume, copie->nume) == 0);
    assert(strcmp(m->producator, copie->producator) == 0);
    assert(m->cantitate == copie->cantitate);
    distrugeMateriePrima(m);
    distrugeMateriePrima(copie);
}

void testGetRepoLen() {
	List* l = creeazaLista(copyMateriePrima,distrugeMateriePrima);
	assert(getLen(l) == 0);
	add(l, creeazaMateriePrima("nume", "producator", 10));
	assert(getLen(l) == 1);
	distrugeLista(l);
}

void testValideazaMateriePrima(void) {
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    assert(valideazaMateriePrima(m) == 1);
    distrugeMateriePrima(m);
    m = creeazaMateriePrima("", "producator", 10);
    assert(valideazaMateriePrima(m) == 0);
    distrugeMateriePrima(m);
    m = creeazaMateriePrima("nume", "", 10);
    assert(valideazaMateriePrima(m) == 0);
    distrugeMateriePrima(m);
    m = creeazaMateriePrima("nume", "producator", -10);
    assert(valideazaMateriePrima(m) == 0);
    distrugeMateriePrima(m);
}

void testCreeazaLista() {
    List* l = creeazaLista(copyMateriePrima,distrugeMateriePrima);
    assert(l->capacitate == 2);
    assert(l->len == 0);
    assert(l->elemente != NULL);
    distrugeLista(l);
}

void testAdaugaMateriePrima() {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    assert(l->len == 1);
    assert(l->capacitate == 2);
    materiePrima* materie = (materiePrima*)l->elemente[0];
    assert(l->elemente[0] == m);
    distrugeLista(l);
}

void testGetLen() {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    assert(getLen(l) == 1);
    distrugeLista(l);
}

void testModificaMaterie(void) {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    materiePrima* m2 = creeazaMateriePrima("nume", "producator2", 20);
    assert(updateMaterie(l, m2) == 1);
    materiePrima* materie = (materiePrima*)l->elemente[0];
    assert(strcmp(materie->producator, "producator2") == 0);
    assert(materie->cantitate == 20);
    materiePrima* m3 = creeazaMateriePrima("nume2", "producator3", 30);
    assert(updateMaterie(l, m3) == 0);
    distrugeMateriePrima(m3);
    distrugeMateriePrima(m2);
    distrugeLista(l);
}

void testStergeMaterie(void) {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    assert(stergeMaterie(l, "nume") == 1);
    assert(getLen(l) == 0);
    assert(stergeMaterie(l, "nume") == 0);
    distrugeLista(l);
}

void testVerificaExistenta(void) {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    assert(verificaExistenta(l, "nume") == 1);
    assert(verificaExistenta(l, "altceva") == 0);
    distrugeLista(l);
}

void testGetMateriePrima(void) {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(l, m);
    assert(getMateriePrimaRepo(l, 0) == m);
    assert(getMateriePrimaRepo(l, 1) == NULL);
    assert(getMateriePrimaRepo(l, -1) == NULL);
    distrugeLista(l);
}

void testResizeRepo(void) {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    add(l, creeazaMateriePrima("item1", "prod1", 10));
    add(l, creeazaMateriePrima("item2", "prod2", 20));
    assert(getLen(l) == 2);
    add(l, creeazaMateriePrima("item3", "prod3", 30));
    assert(getLen(l) == 3);
    assert(l->capacitate == 4);
    distrugeLista(l);
}

void testAdaugaMaterieService() {
    service* s = creeazaRepoService();
    assert(addMaterieService(s, "nume", "producator", 10) == 1);
    assert(getLen(s->repository) == 1);
    assert(addMaterieService(s, "nume", "producator2", 20) == 0);
    assert(getLen(s->repository) == 1);
    materiePrima* materie = (materiePrima*)s->repository->elemente[0];
    assert(strcmp(materie->producator, "producator") == 0);
    assert(materie->cantitate == 10);
    distrugeService(s);
}

void testAdaugaMaterieServiceInvalid() {
    service* s = creeazaRepoService();
    assert(addMaterieService(s, "", "prod", 10) == 0);
    assert(addMaterieService(s, "item", "", 10) == 0);
    assert(addMaterieService(s, "item", "prod", -10) == 0);
    assert(getLen(s->repository) == 0);
    distrugeService(s);
}

void testModificaMaterieService() {
    service* s = creeazaRepoService();
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(s->repository, m);
    assert(modificaMaterieService(s, "nume", "producator2", 20) == 1);
    materiePrima* materie = (materiePrima*)s->repository->elemente[0];
    assert(strcmp(materie->producator, "producator2") == 0);
    assert(materie->cantitate == 20);
    distrugeService(s);
}

void testModificaMaterieServiceInvalid() {
    service* s = creeazaRepoService();
    addMaterieService(s, "item", "prod", 10);
    assert(modificaMaterieService(s, "nonexistent", "newprod", 20) == 0);
    assert(modificaMaterieService(s, "", "newprod", 20) == 0);
    assert(modificaMaterieService(s, "item", "", 20) == 0);
    assert(modificaMaterieService(s, "item", "newprod", -20) == 0);
    distrugeService(s);
}

void testStergeMaterieService() {
    service* s = creeazaRepoService();
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(s->repository, m);
    assert(stergeMaterieService(s, "nume") == 1);
    assert(getLen(s->deletedList) == 1);
    assert(getLen(s->repository) == 0);
    assert(stergeMaterieService(s, "nume") == 0);
    assert(getLen(s->deletedList) == 1);
    distrugeService(s);
}

void testStergeMaterieServiceNonexistent() {
    service* s = creeazaRepoService();
    addMaterieService(s, "item", "prod", 10);
    assert(stergeMaterieService(s, "nonexistent") == 0);
    assert(getLen(s->repository) == 1);
    distrugeService(s);
}

void testFiltrareDupaLitera() {
    service* s = creeazaRepoService();
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(s->repository, m);
    m = creeazaMateriePrima("nume2", "producator2", 20);
    add(s->repository, m);
    List* rez = filtrareDupaLitera(s, 'n');
    assert(getLen(rez) == 2);
    distrugeLista(rez);
    distrugeService(s);
}

void testFiltrareDupaLiteraNoMatch() {
    service* s = creeazaRepoService();
    addMaterieService(s, "abc", "prod", 10);
    addMaterieService(s, "def", "prod", 20);
    List* filtered = filtrareDupaLitera(s, 'z');
    assert(getLen(filtered) == 0);
    distrugeLista(filtered);
    distrugeService(s);
}

void testCmpNume() {
	materiePrima* m1 = creeazaMateriePrima("a", "prod", 10);
	materiePrima* m2 = creeazaMateriePrima("b", "prod", 20);
	assert(cmpNume(m1, m2) == -1);
	assert(cmpNume(m2, m1) == 1);
	assert(cmpNume(m1, m1) == 0);
	distrugeMateriePrima(m1);
	distrugeMateriePrima(m2);
}

void testCmpNumeDesc() {
	materiePrima* m1 = creeazaMateriePrima("a", "prod", 10);
	materiePrima* m2 = creeazaMateriePrima("b", "prod", 20);
	assert(cmpNumeDesc(m1, m2) == 1);
	assert(cmpNumeDesc(m2, m1) == -1);
	assert(cmpNumeDesc(m1, m1) == 0);
	distrugeMateriePrima(m1);
	distrugeMateriePrima(m2);
}

void testCmpCantitate() {
	materiePrima* m1 = creeazaMateriePrima("a", "prod", 10);
	materiePrima* m2 = creeazaMateriePrima("b", "prod", 20);
	assert(cmpCantitate(m1, m2) == -1);
	assert(cmpCantitate(m2, m1) == 1);
	assert(cmpCantitate(m1, m1) == 0);
	distrugeMateriePrima(m1);
	distrugeMateriePrima(m2);
}

void testCmpCantitateDesc() {
	materiePrima* m1 = creeazaMateriePrima("a", "prod", 10);
	materiePrima* m2 = creeazaMateriePrima("b", "prod", 20);
	assert(cmpCantitateDesc(m1, m2) == 1);
	assert(cmpCantitateDesc(m2, m1) == -1);
	assert(cmpCantitateDesc(m1, m1) == 0);
	distrugeMateriePrima(m1);
	distrugeMateriePrima(m2);
}

void testFiltrareDupaCantitate() {
    service* s = creeazaRepoService();
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(s->repository, m);
    m = creeazaMateriePrima("nume2", "producator2", 20);
    add(s->repository, m);
    List* rez = filtrareDupaCantitate(s, 15);
    assert(getLen(rez) == 1);
    distrugeLista(rez);
    distrugeService(s);
}

void testFiltrareDupaCantitateNoMatch() {
    service* s = creeazaRepoService();
    addMaterieService(s, "item1", "prod1", 100);
    addMaterieService(s, "item2", "prod2", 200);
    List* filtered = filtrareDupaCantitate(s, 50);
    assert(getLen(filtered) == 0);
    distrugeLista(filtered);
    distrugeService(s);
}

void testFiltrareDupaProducator()
{
    service* s = creeazaRepoService();
    materiePrima* m = creeazaMateriePrima("nume", "producator", 10);
    add(s->repository, m);
    m = creeazaMateriePrima("nume2", "producator2", 20);
    add(s->repository, m);
    List* rez = filtrareDupaProducator(s, "producator");
    assert(getLen(rez) == 1);
    distrugeLista(rez);
    distrugeService(s);
}
void testFiltrareDupaProducatorNoMatch() {
    service* s = creeazaRepoService();
    addMaterieService(s, "item1", "prod1", 100);
    addMaterieService(s, "item2", "prod2", 200);
    List* filtered = filtrareDupaProducator(s, "prod3");
    assert(getLen(filtered) == 0);
    distrugeLista(filtered);
    distrugeService(s);
}

void testSortare() {
    service* s = creeazaRepoService();
    addMaterieService(s, "faina", "Molino SpA", 100);
    addMaterieService(s, "zahar", "SweetCo", 200);
    addMaterieService(s, "unt", "DairyBest", 150);
    sortare(s, cmpNume);
    materiePrima* materie = (materiePrima*)s->repository->elemente[0];
    materiePrima* materie1 = (materiePrima*)s->repository->elemente[1];
    materiePrima* materie2 = (materiePrima*)s->repository->elemente[2];
    assert(strcmp(materie->nume, "faina") == 0);
    assert(strcmp(materie1->nume, "unt") == 0);
    assert(strcmp(materie2->nume, "zahar") == 0);
    distrugeService(s);
}

void testSortareDesc() {
    service* s = creeazaRepoService();
    addMaterieService(s, "b", "prod", 20);
    addMaterieService(s, "a", "prod", 10);
    sortare(s, cmpNumeDesc);
    materiePrima* materie = (materiePrima*)s->repository->elemente[0];
    materiePrima* materie1 = (materiePrima*)s->repository->elemente[1];
    assert(strcmp(materie->nume, "b") == 0);
    assert(strcmp(materie1->nume, "a") == 0);
    sortare(s, cmpCantitateDesc);
    assert(materie->cantitate == 20);
    assert(materie1->cantitate == 10);
    distrugeService(s);
}

void testGenerareMateriiPrime() {
    service* s = creeazaRepoService();
    generareMateriiPrime(s);
    assert(getLen(s->repository) == 10);
    distrugeService(s);
}

void testGenerareMateriiPrimeDetails() {
    service* s = creeazaRepoService();
    generareMateriiPrime(s);
    assert(getLen(s->repository) == 10);
    int found = 0;
    for (int i = 0; i < getLen(s->repository); i++) 
    {
        materiePrima* materie = (materiePrima*)s->repository->elemente[i];
        if (strcmp(materie->nume, "faina") == 0) {
            found = 1;
            break;
        }
    }
    assert(found == 1);
    distrugeService(s);
}

void testStergeMaterieMuta() {
    List* l = creeazaLista(copyMateriePrima,distrugeMateriePrima);
    add(l, creeazaMateriePrima("item1", "prod1", 10));
    add(l, creeazaMateriePrima("item2", "prod2", 20));
    add(l, creeazaMateriePrima("item3", "prod3", 30));
    assert(getLen(l) == 3);
    materiePrima* materie = (materiePrima*)l->elemente[0];
    materiePrima* materie1 = (materiePrima*)l->elemente[1];
    materiePrima* materie2 = (materiePrima*)l->elemente[2];
    assert(strcmp(materie->nume, "item1") == 0);
    assert(strcmp(materie1->nume, "item2") == 0);
    assert(strcmp(materie2->nume, "item3") == 0);
    assert(stergeMaterie(l, "item2") == 1);
    assert(getLen(l) == 2);
    assert(strcmp(materie->nume, "item1") == 0);
    assert(strcmp(materie2->nume, "item3") == 0);
    distrugeLista(l);
}

void testGetMaterieDupaNume() {
    List* l = creeazaLista(copyMateriePrima, distrugeMateriePrima);
    add(l, creeazaMateriePrima("item1", "prod1", 10));
    add(l, creeazaMateriePrima("item2", "prod2", 20));
    add(l, creeazaMateriePrima("item3", "prod3", 30));
    assert(getLen(l) == 3);
    materiePrima* materie = (materiePrima*)l->elemente[0];
    materiePrima* materie1 = (materiePrima*)l->elemente[1];
    materiePrima* materie2 = (materiePrima*)l->elemente[2];
    assert(strcmp(materie->nume, "item1") == 0);
    assert(strcmp(materie1->nume, "item2") == 0);
    assert(strcmp(materie2->nume, "item3") == 0);
    assert(getMaterieDupaNume(l, "item1") == materie);
    assert(getMaterieDupaNume(l, "item4") == NULL);
    distrugeLista(l);
}

void testUndo() {
    service* s = creeazaRepoService();
    assert(undo(s) == 1);
    addMaterieService(s, "nume", "producator", 100);
    assert(undo(s) == 0);
    distrugeService(s);
}

void test_all() {
    testCreeazaMateriePrima();
    testCopyMateriePrima();
    testValideazaMateriePrima();
    testCmpNumeDesc();
    testCreeazaLista();
    testCreeazaRepoService();
    testAdaugaMateriePrima();
    testGetLen();
    testModificaMaterie();
    testStergeMaterie();
    testVerificaExistenta();
    testGetMateriePrima();
    testResizeRepo();
    testCmpNume();
    testAdaugaMaterieService();
    testAdaugaMaterieServiceInvalid();
    testModificaMaterieService();
    testModificaMaterieServiceInvalid();
    testStergeMaterieService();
    testStergeMaterieServiceNonexistent();
    testCmpCantitateDesc();
    testFiltrareDupaLitera();
    testFiltrareDupaLiteraNoMatch();
    testFiltrareDupaCantitate();
    testFiltrareDupaCantitateNoMatch();
    testFiltrareDupaProducator();
    testFiltrareDupaProducatorNoMatch();
    testCmpCantitate();
    testSortare();
    testSortareDesc();
    testGetRepoLen();
    testGenerareMateriiPrime();
    testGenerareMateriiPrimeDetails();
	testStergeMaterieMuta();
    testUndo();
    testGetMaterieDupaNume();
    printf("\n");
    printf("\nTestele au rulat cu succes!\n");
}
