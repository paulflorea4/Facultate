apare([H|_],H):-!.
apare([_|T],E):-
	apare(T,E).

verif([],_):-!.
verif([H|T],L):-
	apare(L,H),!,
	verif(T,L).

main(L1,L2):-
	verif(L1,L2),
	verif(L2,L1).

selecteaza([H|_],N,H):-
	N =:= 1,!.
selecteaza([_|T],N,R):-
	N1 is N-1,
	selecteaza(T,N1,R).
