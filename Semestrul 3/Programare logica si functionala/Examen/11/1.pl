%putere(X,N,R)
putere(_,0,1):-!.
putere(X,N,R):-
	N1 is N-1,
	putere(X,N1,R1),
	R is X*R1.

