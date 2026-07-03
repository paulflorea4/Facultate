reuniune([],L,L).
reuniune([H|T],L,R):-
	member(H,L),
	reuniune(T,L,R).
reuniune([H|T],L,[H|R]):-
	\+ member(H,L),
	reuniune(T,L,R).


/*
pereche(l1...ln) = [] , daca n = 0 sau n=1
                 = pereche_aux(l1,l2...ln), altfel

pereche_aux(x,l1...ln) = [] , daca n=0
		       = [x,l1] , daca n=1
		       = [x,l1] + pereche_aux(x,l2...ln), altfel
*/
perechi([],[]):-!.
perechi([_],[]):-!.
perechi([H|T],R):-
	pereche_aux(H,T,R1),
	perechi(T,R2),
	append(R1, R2, R).

pereche_aux(_,[],[]):-!.
pereche_aux(X,[Y|T],[[X,Y]|R]):-
	pereche_aux(X,T,R).
