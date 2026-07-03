#include "IteratorMultime.h"
#include "Multime.h"
#include <exception>

IteratorMultime::IteratorMultime(Multime& m) : multime(m) {
    prim();
}

void IteratorMultime::prim() {
    curent = multime.prim;
}

void IteratorMultime::urmator() {
    if (!valid()) 
        throw std::exception();
    curent = multime.urm[curent];
}

bool IteratorMultime::valid() const {
    return curent != -1;
}

TElem IteratorMultime::element() const {
    if (!valid()) 
        throw std::exception();
    return multime.elems[curent];
}

TElem IteratorMultime::elimina() {
    //BC:elementul curent e primul -> theta(1)
    //WC:elementul curent e ultimul -> theta(n)
    //AC:O(n)
    if (!valid())
        throw std::exception();

    TElem val = multime.elems[curent];

    if (curent == multime.prim) {
        int el = curent;
        curent = multime.urm[curent];
        multime.prim = curent;
        multime.dealoca(el);
    }
    else {
        int anterior = multime.prim;
        while (anterior != -1 && multime.urm[anterior] != curent) {
            anterior = multime.urm[anterior];
        }

        int el = curent;
        curent = multime.urm[curent];
        multime.urm[anterior] = curent;
        multime.dealoca(el);
    }

    return val;

    /*
    * subalgoritm elimina(){
    *   daca nu e valid atunci
    *       arunca exceptie
    *   sf daca
    *   val<-multime.elems[curent]
    *   daca curent=multime.prim atunci
    *       el<-curent
    *       curent-<multime.urm[curent]
    *       multime.prim=curent
    *       multime.dealoca(el)
    *   altfel
    *       anterior=multime.prim
    *       cat timp anterior != -1 si multime.urm[anterior]!=curent executa
    *           anterior=multime.urm[anterior]
    *       sf cat timp
    *       el<-curent
    *       curent<-multime.urm[curent]
    *       multime.urm[anterior]=curent
    *       multime.dealoca(el)
    *   sf daca
    *   return val
    * sf subalgoritm
    */
}
