/*
a.
exista(l1...ln,x)= - fals, n=0
                   - adv , l1=x
		   - exista(l2...ln,x), altfel

diferenta(a1...an,b1...bm)= - [] , daca n=0
			    - a1 (+) diferenta(a2...an,b1...bm),daca exista(b1...bm,a1) e fals
			    - diferenta(a2...an,b1...bm),altfel
*/


exista([H|_],X):-
	H=:=X,!.

exista([_|T],X):-
	exista(T,X).

diferenta([],_,[]):-!.

diferenta([H|T],L,[H|Rez]):-
	\+ exista(L,H),!,
	diferenta(T,L,Rez).
diferenta([_|T],L,Rez):-
	diferenta(T,L,Rez).

/*
b.
adauga1(l1...ln)= -[] , daca n=0
		  -l1 (+) 1 (+) adauga1(l2...ln),daca l1 e par
		  -l1 (+) adauga1(l2...ln), altfel

*/

adauga1([],[]):-!.

adauga1([H|T],[H,1|Rez]):-
	0 is H mod 2,!,
	adauga1(T,Rez).

adauga1([H|T],[H|Rez]):-
	adauga1(T,Rez).

%main(L: lista)
%(i)
main(L) :-
	vale(L, 0, 0, 0).

%vale(L: lista, monotonie: int, c: int, d: int)
%(i, i, i, i) (i, i, i, i)
vale([_], M, C, D) :-
	M =:= 1,
	C >= 1,
	D >= 1.
vale([E1, E2 | L], _, C, D) :-
	E1 < E2,
	C1 is C + 1,
	vale([E2 | L], 1, C1, D).
vale([E1, E2 | L], M, C, D) :-
	E1 > E2,
	M =:= 0,
	vale([E2 | L], 0, C, D1),
	D1 is D + 1.
