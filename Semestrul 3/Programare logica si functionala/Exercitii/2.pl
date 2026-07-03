/*
a.
cmmdc(x,y)= - x , daca x=y
            - cmmdc(x-y,y) , daca x>y
	    - cmmdc(x,y-x), altfel

cmmmc(x,y) = x*y / cmmdc(x,y)

cmmmc_lista(l1...ln)= - l1 , daca n=1
                      - cmmmc(l1,cmmmc_lista(l2...ln)), altfel
*/

cmmdc(X,X,X):-!.

cmmdc(X, 0, X) :- !.

cmmdc(0, Y, Y) :- !.

cmmdc(X,Y,R):-
	X>Y,!,
	Z is X-Y,
	cmmdc(Z,Y,R).

cmmdc(X,Y,R):-
	Z is Y-X,
	cmmdc(X,Z,R).

cmmmc(X,Y,R):-
	cmmdc(X,Y,D),
	R is abs(X*Y)//D.

cmmmc_lista([],0):-!.

cmmmc_lista([X],X):-!.

cmmmc_lista([H|T],R):-
	cmmmc_lista(T,R1),
	cmmmc(H,R1,R).

/*
b.
adauga(l1...ln,idx,ord,v)= - lista vida , n=0
			   - l1 (+) v (+) adauga(l2...ln,2*idx,ord+1,v),daca idx=ord
			   - l1 (+) adauga(l2...ln,idx,ord+1,v),altfel

main(l1...ln,v)=adauga(l1...ln,1,1,v)
*/

adauga([],_,_,_,[]):-!.
adauga([H|T],Idx,Idx,Val,[H,Val|Rez]):-!,
       Idx2 is 2*Idx,
       Idx3 is Idx+1,
       adauga(T,Idx2,Idx3,Val,Rez).

adauga([H|T],Idx,Idx2,Val,[H|Rez]):-
	Idx3 is Idx2+1,
	adauga(T,Idx,Idx3,Val,Rez).

main(L,V,Rez):-
	adauga(L,1,1,V,Rez).



