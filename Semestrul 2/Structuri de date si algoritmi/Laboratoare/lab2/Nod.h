#pragma once
#include <utility>
typedef int TCheie;
typedef int TValoare;
typedef std::pair<TCheie, TValoare> TElem;

class Nod;

typedef Nod* PNod;

class Nod {
    friend class MD;      
    friend class IteratorMD;

private:
    TElem e;
    PNod urm;
    PNod prec;

public:
    Nod(TElem e, PNod urm = nullptr, PNod prec = nullptr);
    TElem element() const;
    PNod urmator() const;
    PNod precedent() const;
};
