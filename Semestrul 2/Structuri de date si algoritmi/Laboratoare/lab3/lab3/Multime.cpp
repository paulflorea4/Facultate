#include "Multime.h"
#include "IteratorMultime.h"

#include <iostream>

Multime::Multime() {
    capacitate = 100;
    elems = new TElem[capacitate];
    urm = new int[capacitate];
    prim = -1;
    primLiber = 0;
    for (int i = 0; i < capacitate - 1; ++i)
        urm[i] = i + 1;
    urm[capacitate - 1] = -1;
}

int Multime::aloca() {
    if (primLiber == -1)
        redimensionare();
    int i = primLiber;
    primLiber = urm[i];
    return i;
}

void Multime::dealoca(int i) {
    urm[i] = primLiber;
    primLiber = i;
}

void Multime::redimensionare() {
    int nouaCap = capacitate * 2;
    TElem* elemsNoi = new TElem[nouaCap];
    int* urmNou = new int[nouaCap];

    for (int i = 0; i < capacitate; ++i) {
        elemsNoi[i] = elems[i];
        urmNou[i] = urm[i];
    }
    for (int i = capacitate; i < nouaCap - 1; ++i)
        urmNou[i] = i + 1;
    urmNou[nouaCap - 1] = -1;

    delete[] elems;
    delete[] urm;

    elems = elemsNoi;
    urm = urmNou;
    primLiber = capacitate;
    capacitate = nouaCap;
}

bool Multime::adauga(TElem e) {
    if (cauta(e)) 
        return false;
    int i = aloca();
    elems[i] = e;
    urm[i] = prim;
    prim = i;
    return true;
}

bool Multime::sterge(TElem e) {
    int curent = prim, anterior = -1;
    while (curent != -1 && elems[curent] != e) {
        anterior = curent;
        curent = urm[curent];
    }
    if (curent == -1) 
        return false;

    if (anterior == -1)
        prim = urm[curent];
    else
        urm[anterior] = urm[curent];

    dealoca(curent);
    return true;
}

bool Multime::cauta(TElem e) const {
    int i = prim;
    while (i != -1) {
        if (elems[i] == e)
            return true;
        i = urm[i];
    }
    return false;
}

int Multime::dim() const {
    int count = 0, i = prim;
    while (i != -1) {
        count++;
        i = urm[i];
    }
    return count;
}

bool Multime::vida() const {
    return prim == -1;
}

IteratorMultime Multime::iterator(){
    return IteratorMultime(*this);
}

Multime::~Multime() {
    delete[] elems;
    delete[] urm;
}

    