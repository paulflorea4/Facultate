#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <cmath>
#include <iostream>
using namespace std;
#include <vector>

Colectie::Colectie() {
	//BC=WC=AC -> theta(n)
    cp = 13;
    e = new TElem[cp];
    urm = new int[cp];
    frecv = new int[cp];
    primLiber = 0;

    for (int i = 0; i < cp; i++) {
        e[i] = NULL_TELEM;
        urm[i] = NULL_POS;
        frecv[i] = 0;
    }
}

int Colectie::hash(TElem elem) const {
	//BC=WC=AC -> theta(1)
    return abs(elem) % cp;
}

void Colectie::actPrimLiber() {
	//BC:lista e goala -> theta(1)
	//WC:lista e plina -> theta(n)
	//AC:O(n)
    primLiber=0;
    while (primLiber < cp && e[primLiber] != NULL_TELEM)
        primLiber++;
}

void Colectie::redimensionare() {
	//BC=WC=AC -> theta(n)
    int oldCp = cp;
    cp *= 2;

    TElem* oldE = e;
    int* oldUrm = urm;
    int* oldFrecv = frecv;

    e = new TElem[cp];
    urm = new int[cp];
    frecv = new int[cp];

    for (int i = 0; i < cp; i++) {
        e[i] = NULL_TELEM;
        urm[i] = NULL_POS;
        frecv[i] = 0;
    }

    primLiber = 0;

    for (int i = 0; i < oldCp; i++) {
        if (oldE[i] != NULL_TELEM) {
            for (int j = 0; j < oldFrecv[i]; j++) {
                adauga(oldE[i]);
            }
        }
    }

    delete[] oldE;
    delete[] oldUrm;
    delete[] oldFrecv;
}

void Colectie::adauga(TElem elem) {
	//BC:pozitia corespunzatoare e libera-> theta(1)
	//WC:elementul nu exista in colectie -> theta(n)
	//AC:O(n)
    int i = hash(elem);

    if (e[i] == NULL_TELEM) {
        e[i] = elem;
        frecv[i] = 1;
        if (i == primLiber) actPrimLiber();
        return;
    }

    int p = i;
    int prev = NULL_POS;

    while (p != NULL_POS) {
        if (e[p] == elem) {
            frecv[p]++;
            return;
        }
        prev = p;
        p = urm[p];
    }

    if (primLiber == cp) {
        redimensionare();
        adauga(elem);
        return;
    }

    e[primLiber] = elem;
    frecv[primLiber] = 1;
    urm[primLiber] = NULL_POS;
    urm[prev] = primLiber;

    actPrimLiber();
}

bool Colectie::sterge(TElem elem) {
    //BC:theta(1)
    //WC:theta(n)
    //AC:O(n)
    int i = hash(elem);
    int p = i;
    int prev = -1;

    while (p != NULL_POS && e[p] != elem) {
        prev = p;
        p = urm[p];
    }

    if (p == NULL_POS)
        return false;

    frecv[p]--;
    if (frecv[p] > 0)
        return true;

    const int MAX_REINSERATE = cp;
    TElem* valSalvate = new TElem[MAX_REINSERATE];
    int* frecvSalvate = new int[MAX_REINSERATE];
    int count = 0;

    int q = urm[p];
    while (q != NULL_POS) {
        valSalvate[count] = e[q];
        frecvSalvate[count] = frecv[q];
        count++;

        int urmQ = urm[q];
        e[q] = NULL_TELEM;
        frecv[q] = 0;
        urm[q] = NULL_POS;
        q = urmQ;
    }

    if (prev != -1) {
        urm[prev] = NULL_POS;
    }
    else {
        for (int k = 0; k < cp; ++k) {
            if (urm[k] == p) {
                urm[k] = urm[p];
                break;
            }
        }
    }

    e[p] = NULL_TELEM;
    frecv[p] = 0;
    urm[p] = NULL_POS;

    if (p < primLiber)
        primLiber = p;

    for (int idx = 0; idx < count; ++idx) {
        for (int j = 0; j < frecvSalvate[idx]; ++j) {
            adauga(valSalvate[idx]);
        }
    }

    delete[] valSalvate;
    delete[] frecvSalvate;

    return true;
}

//bool Colectie::sterge(TElem elem) {
//    int i = hash(elem);
//    int p = i;
//    int prev = -1;
//
//    // căutăm elementul în lanț
//    while (p != NULL_POS && e[p] != elem) {
//        prev = p;
//        p = urm[p];
//    }
//
//    if (p == NULL_POS)
//        return false;
//
//    frecv[p]--;
//    if (frecv[p] > 0)
//        return true;
//
//    // ștergem complet elementul
//    while (true) {
//        int q = urm[p];
//        int q_prev = p;
//
//        // căutăm primul element care s-a dispersat pe p
//        while (q != NULL_POS && hash(e[q]) != p) {
//            q_prev = q;
//            q = urm[q];
//        }
//
//        if (q == NULL_POS) {
//            // nu mai avem ce muta în p, deci ștergem
//            if (prev != -1) {
//                urm[prev] = urm[p];
//            }
//            else {
//                // dacă p este început de lanț, vedem cine îl referă
//                for (int k = 0; k < cp; ++k) {
//                    if (urm[k] == p) {
//                        urm[k] = urm[p];
//                        break;
//                    }
//                }
//            }
//
//            e[p] = NULL_TELEM;
//            frecv[p] = 0;
//            urm[p] = NULL_POS;
//
//            if (p < primLiber)
//                primLiber = p;
//
//            return true;
//        }
//        else {
//            // mutăm e[q] în p
//            e[p] = e[q];
//            frecv[p] = frecv[q];
//            urm[p] = urm[q];
//
//            // refacem legătura către q
//            if (q_prev != p)
//                urm[q_prev] = urm[q];
//
//            // continuăm ștergerea de la q
//            prev = q_prev;
//            p = q;
//        }
//    }
//}

bool Colectie::cauta(TElem elem) const {
	//BC:elementul nu exista in colectie -> theta(1)
	//WC:elementul exista in colectie -> theta(n)
	//AC:O(n)
    int p = hash(elem);
    while (p != NULL_POS) {
        if (e[p] == elem)
            return true;
        p = urm[p];
    }
    return false;
}

int Colectie::nrAparitii(TElem elem) const {
	//BC:elementul nu exista in colectie -> theta(1)
	//WC:elementul exista in colectie -> theta(n)
	//AC:O(n)
    int p = hash(elem);
    while (p != NULL_POS) {
        if (e[p] == elem)
            return frecv[p];
        p = urm[p];
    }
    return 0;
}

int Colectie::dim() const {
	//BC=WC=AC -> theta(n)
    int total = 0;
    for (int i = 0; i < cp; i++) {
        if (e[i] != NULL_TELEM)
            total += frecv[i];
    }
    return total;
}

bool Colectie::vida() const {
	//BC=WC=AC -> theta(1)
    return dim() == 0;
}

IteratorColectie Colectie::iterator() const {
    //BC=WC=AC -> theta(1)
    return IteratorColectie(*this);
}

Colectie::~Colectie() {
    delete[] e;
    delete[] urm;
    delete[] frecv;
}

int Colectie::diferenta() const {
	//BC:Colectia e vida -> theta(1)
	//WC:Colectia nu e vida -> theta(n)
	//AC:O(n)
    if (vida()) return -1;

    TElem minim = INT_MAX;
    TElem maxim = INT_MIN;

    for (int i = 0; i < cp; i++) {
        if (frecv[i] > 0) {
            if (e[i] < minim) minim = e[i];
            if (e[i] > maxim) maxim = e[i];
        }
    }

    return maxim - minim;
    /*
    * Subalgoritm diferenta()
    *   daca e vida atunci
    *       returneaza -1
    *   sf daca
	*   minim<-INT_MAX
	*   maxim<-INT_MIN
    *   pentru i=0,cp executa
	*       daca frecv[i]>0 atunci
	*           daca e[i]<minim atunci
	*               minim<- e[i]
    *           sf daca
	*           daca e[i]>maxim atunci
	*               maxim<- e[i]
    *           sf daca
	*       sf daca
	*   sf pentru
	*   returneaza maxim-minim
    */
}
