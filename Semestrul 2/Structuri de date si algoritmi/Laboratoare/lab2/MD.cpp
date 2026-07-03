#include "MD.h"
#include "IteratorMD.h"

using namespace std;

MD::MD() : prim(nullptr), ultim(nullptr), dimensiune(0) {}

void MD::adauga(TCheie c, TValoare v) {
    //BC=WC=AC
    //theta(1)
    PNod nou = new Nod({ c, v }, nullptr, ultim);
    if (ultim != nullptr)
        ultim->urm = nou;
    else
        prim = nou;
    ultim = nou;
    dimensiune++;
}

bool MD::sterge(TCheie c, TValoare v) {
    //BC:elementul e primul -> theta(1)
    //WC:elementul e ultimul sau nu exita -> theta(n)
    //AC:O(n)
    PNod curent = prim;
    while (curent != nullptr) {
        if (curent->e.first == c && curent->e.second == v) {
            if (curent->prec != nullptr)
                curent->prec->urm = curent->urm;
            else
                prim = curent->urm;

            if (curent->urm != nullptr)
                curent->urm->prec = curent->prec;
            else
                ultim = curent->prec;

            delete curent;
            dimensiune--;
            return true;
        }
        curent = curent->urm;
    }
    return false;
}

vector<TValoare> MD::cauta(TCheie c) const {
    //BC:elementul e primul -> theta(1)
    //WC:elementul e ultimul sau nu exita -> theta(n)
    //AC:O(n)
    vector<TValoare> rez;
    PNod curent = prim;
    while (curent != nullptr) {
        if (curent->e.first == c)
            rez.push_back(curent->e.second);
        curent = curent->urm;
    }
    return rez;
}

int MD::dim() const {
    //BC=WC=AC
    //theta(1)
    return dimensiune;
}

bool MD::vid() const {
    //BC=WC=AC
    //theta(1)
    return dimensiune == 0;
}

IteratorMD MD::iterator() const {
    //BC=WC=AC
    //theta(1)
    return IteratorMD(*this);
}

MD::~MD() {
    //BC=WC=AC
    //theta(n)
    while (prim != nullptr) {
        PNod aux = prim;
        prim = prim->urm;
        delete aux;
    }
}
