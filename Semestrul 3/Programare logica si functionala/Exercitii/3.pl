adauga([],_,[]):-!.
adauga([H|T],Rest,[H|R]):-
	Rest is H mod 2,!,
	adauga(T,Rest,R).
adauga([_|T],Rest,R):-
	adauga(T,Rest,R).

descompune(L,[R1,R2],P,I):-
	adauga(L,0,R1),
	length(R1,P),
	adauga(L,1,R2),
	length(R2,I).
