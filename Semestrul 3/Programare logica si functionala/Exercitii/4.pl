elimina([],[]):-!.
elimina([_],[]):-!.
elimina([H|T],[H|R]):-
	elimina(T,R).
