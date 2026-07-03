#include "IteratorColectie.h"
#include <exception>
#include <iostream>

IteratorColectie::IteratorColectie(const Colectie& c) : col(c) {
    prim();
}

void IteratorColectie::prim() {
	//BC:colectia e vida -> theta(1)
	//WC:colectia e plina -> theta(n)
	//AC:O(n)
    curent = 0;
    while (curent < col.cp && col.frecv[curent] == 0)
        curent++;
    frecvCurenta = (curent < col.cp ? col.frecv[curent] : 0);
}

void IteratorColectie::urmator() {
	//BC:iteratorul e invalid -> theta(1)
	//WC:iteratorul e valid -> theta(n)
	//AC:O(n)
    if (!valid()) throw std::exception();
    frecvCurenta--;
    if (frecvCurenta == 0) {
        curent++;
        while (curent < col.cp && col.frecv[curent] == 0)
            curent++;
        if (curent < col.cp)
            frecvCurenta = col.frecv[curent];
    }
}

bool IteratorColectie::valid() const {
	//BC=WC=AC -> theta(1)
    return curent < col.cp;
}

TElem IteratorColectie::element() const {
	//BC=WC=AC -> theta(1)
    if (!valid()) throw std::exception();
    return col.e[curent];
}
