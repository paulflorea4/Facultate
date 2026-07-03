candidat([H|_],H,[]).
candidat([H|T],E,[H|Rest]):-
	candidat(T,E,Rest).

generare(_,Col,Col).
generare(L,[C1|Col],Rez):-
	candidat(L,E,Rest),
	E<C1,
	generare(Rest,[E,C1|Col],Rez).

subsir_candidat(L,R):-
	candidat(L,E,Rest),
	generare(Rest,[E],R).

main(L,R):-
	findall(R1,subsir_candidat(L,R1),R).
