aparitii([],_,0).
aparitii([H|T],H,Nr):-!,
	aparitii(T,H,Nr1),
	Nr is Nr1+1.
aparitii([_|T],E,Nr):-
	aparitii(T,E,Nr).

verificare([]):-!.
verificare([H|T]):-
	aparitii(T,H,R),
	R=0,
	verificare(T).


sterge([],_,_,[]).
sterge([H|T],H,3,[H|R]):-!,
	sterge(T,H,3,R).
sterge([H|T],E,Nr,[H|R]):-
	H=\=E,!,
	sterge(T,E,Nr,R).
sterge([H|T],H,Nr,R):-
	Nr<3,!,
	Nr1 is Nr+1,
	sterge(T,H,Nr1,R).
