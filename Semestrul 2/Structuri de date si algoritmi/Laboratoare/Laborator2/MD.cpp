#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;


MD::MD() {
	capacitate = 10;
    elems = new TElem[capacitate];
    urm = new int[capacitate];
    prec = new int[capacitate];
    for (int i = 0; i < capacitate - 1; ++i)
        urm[i] = i + 1;
    urm[capacitate - 1] = -1;
    primLiber = 0;
    prim = -1;
    ultim = -1;
    dimensiune = 0;
}

int MD::aloca() {
    if (primLiber == -1) redimensionare();
    int i = primLiber;
    primLiber = urm[primLiber];
    return i;
}

void MD::dealoca(int i) {
    urm[i] = primLiber;
    primLiber = i;
}

bool MD::sterge(TCheie c, TValoare v) {
	/* de adaugat */
	return false;
}

void MD::redimensionare() {
    int capacitateNoua = capacitate * 2;
    TElem* elemsNoi = new TElem[capacitateNoua];
    int* urmNou = new int[capacitateNoua];
    int* precNou = new int[capacitateNoua];

    for (int i = 0; i < capacitate; ++i) {
        elemsNoi[i] = elems[i];
        urmNou[i] = urm[i];
        precNou[i] = prec[i];
    }
    for (int i = capacitate; i < capacitateNoua - 1; ++i)
        urmNou[i] = i + 1;
    urmNou[capacitateNoua - 1] = -1;

    delete[] elems;
    delete[] urm;
    delete[] prec;

    elems = elemsNoi;
    urm = urmNou;
    prec = precNou;
    primLiber = capacitate;
    capacitate = capacitateNoua;
}

void MD::adauga(TCheie c, TValoare v) {
    int i = aloca();
    elems[i] = { c, v };
    urm[i] = -1;
    prec[i] = ultim;

    if (ultim != -1)
        urm[ultim] = i;
    else
        prim = i;

    ultim = i;
    dimensiune++;
}

vector<TValoare> MD::cauta(TCheie c) const {
	vector<TValoare> rez;
    int p = prim;
    while (p != -1) {
        if (elems[p].first == c)
            rez.push_back(elems[p].second);
        p = urm[p];
    }
    return rez;
}


int MD::dim() const {
	return dimensiune;
}


bool MD::vid() const {
	return dimensiune == 0;
}

IteratorMD MD::iterator() const {
	return IteratorMD(*this);
}


MD::~MD() {
	delete[] elems;
    delete[] urm;
    delete[] prec;
}

