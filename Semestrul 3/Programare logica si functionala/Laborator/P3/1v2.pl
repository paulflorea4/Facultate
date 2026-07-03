coliniar([[_,_]]):-!.
coliniar([[X1,Y1],[X2,Y2]|Rest]):-
	coliniar_aux([X1,Y1],[X2,Y2],Rest).

coliniar_aux(_,_,[]):-!.
coliniar_aux([X1,Y1],[X2,Y2],[[X3,Y3]|Rest]):-
	(Y3-Y1)*(X2-X1)=:=(Y2-Y1)*(X3-X1),
	coliniar_aux([X1,Y1],[X2,Y2],Rest).

candidat([H|_],H).
candidat([_|T],Rez):-
	candidat(T,Rez).

valid(_,L):-
	length(L,N),
	N<2,!.

valid(P,L):-
	coliniar([P|L]).

generare(_,Col,Col):-
	length(Col,L),
	L >= 3.

generare(Puncte,Col,Rez):-
	candidat(Puncte,P),
	\+ member(P,Col),
	valid(P,Col),
	generare(Puncte,[P|Col],Rez).

submultime_coliniara(Puncte,Rez):-
	findall(R1,generare(Puncte,[],R1),Rez).
