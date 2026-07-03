aparitii([],_,0):-!.
aparitii([H|T],H,R):-!,
	aparitii(T,H,R1),
	R is R1+1.
aparitii([_|T],E,R):-
	aparitii(T,E,R).

stergeRepetitii([],[]).
stergeRepetitii([H|T],[H|R]):-
	aparitii([H|T],H,N),
	N=:=1,!,
	stergeRepetitii(T,R).
stergeRepetitii([_|T],R):-
	stergeRepetitii(T,R).

/*
maxim(l1...ln,Max)=Max,n=0
                   maxim(l2...ln,l1),daca l1>Max
		   maxim(l2...ln,Max),altfel

main(l1...ln)=0,daca n=0
	     =maxim(l2...ln,l1),altfel
*/

maxim([X],X):-!.
maxim([H|T],M):-
	maxim(T,M1),
	M is max(H,M1).

sterge([],_,[]).
sterge([H|T],H,R):-!,
	sterge(T,H,R).
sterge([H|T],E,[H|R]):-
	sterge(T,E,R).

main([],[]):-!.
main(L,R):-
	maxim(L,M),
	sterge(L,M,R).
