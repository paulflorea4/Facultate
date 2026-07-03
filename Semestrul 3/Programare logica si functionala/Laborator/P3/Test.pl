%creeaza(E:element,N:intreg,R:lista)
creeaza(_,0,[]):-!.
creeaza(E,N,[E|R]):-
	N1 is N-1,
	creeaza(E,N1,R).

/*
adaugaLista(l1...ln,M,e,idx,nr) = [] , daca n=0 si M != idx
				= creeaza(e,nr) , daca n=0 si M=idx
	  = creeaza(e,nr) (+) l1 (+) adaugaLista(l2...ln,M,e,1,nr+1), daca M=idx
	  = l1 (+) adaugaLista(l2...ln,M,e,idx+1,nr) , altfel

creeaza(e,nr)= [] , daca nr = 0
	     = e (+) creeaza(e,nr-1) , altfel

main(l1...ln,M,e)=adaugaLista(l1...ln,M,e,1,1)

*/
%adauga(L:lista,M:intreg,E:intreg,Nr:intreg,R:Lista)
%model de flux: (i,i,i,i,i,i),(i,i,i,i,i,o)
adaugaLista([],M,E,Idx,Nr,R):-
	creeaza(E,Nr,R),
	M is Idx,!.
adaugaLista([],_,_,_,_,[]):-!.
adaugaLista([H|T],M,E,Idx,Nr,[R1,H|R]):-
	M is Idx,!,
	creeaza(E,Nr,R1),
	Nr1 is Nr+1,
	adaugaLista(T,M,E,1,Nr1,R).
adaugaLista([H|T],M,E,Idx,Nr,[H|R]):-
	Idx1 is Idx+1,
	adaugaLista(T,M,E,Idx1,Nr,R).

%main(L:lista,M:intreg,E:intreg,R:Lista)
%model de flux:
main(L,M,E,R):-
	adaugaLista(L,M,E,0,1,R).
