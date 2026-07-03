#include "IteratorMD.h"
#include <exception>

IteratorMD::IteratorMD(const MD& d) : md(d), curent(d.prim) {}

void IteratorMD::prim() {
    //BC=WC=AC
    //theta(1)
    curent = md.prim;
}

void IteratorMD::urmator() {
    //BC=WC=AC
    //theta(1)
    if (!valid())
        throw std::exception();
    curent = curent->urm;
}

bool IteratorMD::valid() const {
    //BC=WC=AC
    //theta(1)
    return curent != nullptr;
}

TElem IteratorMD::element() const {
    //BC=WC=AC
    //theta(1)
    if (!valid())
        throw std::exception();
    return curent->element();
}

void IteratorMD::jumpForward(int k) {
    //BC:iteratorl e invalid sau k<=0 -> theta(1)
    //WC:in caz contrar -> theta(k)
    //AC:O(k)

    if (!valid())
        throw std::exception();
    if (k <= 0)
        throw std::exception();

    int pasi = 0;
    while (curent != nullptr && pasi < k) {
        curent = curent->urm;
        pasi++;
    }

    /*
    * Algoritm jumpForward(k)
    *       Daca !valid() atunci
    *           arunca exceptie
    *       Sf daca
    *       Daca k<=0 atucni
    *           arunca exceptie
    *       Sf daca
    *       pasi=0
    *       Cat timp curent!=null si pasi<k executa
    *           curent=[curent].urm
    *           pasi=pasi+1
    *       Sf cat timp
    * Sfarsit algoritm
    */
}
