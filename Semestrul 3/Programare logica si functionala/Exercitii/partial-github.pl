plaseaza([],X,[X]):-!.
plaseaza([H|T],X,[H|R]):-
	X>H,!,
	plaseaza(T,X,R).
plaseaza([H|T],X,[X,H|T]):-
	X<H,!.
plaseaza([H|T],X,[H|T]):-
	X =:= H,!.

sorteaza([],[]):-!.
sorteaza([H|T],R):-
	sorteaza(T,R1),
	plaseaza(R1,H,R).

