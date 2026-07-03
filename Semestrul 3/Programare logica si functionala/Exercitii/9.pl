intersectie([],_,[]).
intersectie([H|T],L,[H|R]):-
	member(H,L),!,
	intersectie(T,L,R).
intersectie([_|T],L,R):-
	intersectie(T,L,R).


constr(M,N,[]):-
	M>N,!.
constr(M,N,[M|R]):-
	M1 is M+1,
	constr(M1,N,R).
