candidat([H|_],H).
candidat([_|T],Rez):-
	candidat(T,Rez).

generare(_,K,Col,Col):-
	length(Col,N),
	N=:=K.
generare(L,K,Col,R):-
	length(Col,N),
	N<K,
	candidat(L,C),
	\+ candidat(Col,C),
	generare(L,K,[C|Col],R).

main(L,K,R):-
	findall(R1,generare(L,K,[],R1),R).

