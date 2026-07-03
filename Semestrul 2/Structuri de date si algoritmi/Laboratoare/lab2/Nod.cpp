#include "Nod.h"

Nod::Nod(TElem elem, PNod u, PNod p) : e(elem), urm(u), prec(p) {}

TElem Nod::element() const {
    return e;
}

PNod Nod::urmator() const {
    return urm;
}

PNod Nod::precedent() const {
    return prec;
}
