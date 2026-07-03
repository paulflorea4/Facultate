#include "IteratorMD.h"
#include "MD.h"

using namespace std;

IteratorMD::IteratorMD(const MD& _md): md(_md) {
	curent=md.prim;
}

TElem IteratorMD::element() const{
	if (!valid())
          throw exception();
    return md.elems[curent];
}

bool IteratorMD::valid() const {
	return curent != -1;
}

void IteratorMD::urmator() {
	if (!valid())
          throw exception();
    curent = md.urm[curent];
}

void IteratorMD::prim() {
	curent = md.prim;
}

