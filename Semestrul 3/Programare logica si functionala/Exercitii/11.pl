suma_alternanta([],_,0):-!.
suma_alternanta([H|T],0,R):-!,
	suma_alternanta(T,1,R1),
	R is R1+H.
suma_alternanta([H|T],1,R):-
	suma_alternanta(T,0,R1),
	R is R1-H.


