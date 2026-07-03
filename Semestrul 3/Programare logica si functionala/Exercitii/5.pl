apare([H|_],H):-!.
apare([_|T],E):-
	apare(T,E).

aparitii([],_,0).
aparitii([H|T],H,R):-!,
	aparitii(T,H,R1),
	R is R1+1.
aparitii([_|T],E,R):-
	aparitii(T,E,R).

adauga_aux([],_,[]).
adauga_aux([H|T],V,[[H,A]|R]):-
	\+ apare(V,H),!,
	aparitii([H|T],H,A),
	adauga_aux(T,[H|V],R).
adauga_aux([_|T],V,R):-
	adauga_aux(T,V,R).

adauga(L,R):-
	adauga_aux(L,[],R).
