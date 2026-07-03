;l-lista de numere liniara
;d-bit pentru monotonie 0-cresc 1-desc
;m-contor pentru schimbare de monotonie
;u-ultimul element citit

(defun vale(l d m u)
	(cond
		((and (null l) (= m 1)) t)
		((null l) nil)
		((and (> u (car l)) (= d 1)) (vale (cdr l) 1 m (car l)))
		((> u (car l)) (vale (cdr l) 1 (+ m 1) (car l)))
		((and (< u (car l)) (= d 0)) (vale (cdr l) 0 m (car l)))
		(t (vale (cdr l) 0 (+ m 1) (car l)))
	)
)

;l-lista de atomi liniara
(defun numerici(l)
	(cond
	((null l) nil)
	((numberp (car l)) (cons (car l) (numerici (cdr l))))
	(t (numerici (cdr l)))
	)
)

;l-lista de atomi liniara
(defun main(l)
	(and (> (car (numerici l)) (cadr (numerici l))) 
		(vale (cdr (numerici l)) 1 0 (car (numerici l))))
)

#|
numerici(l1...ln)=[],daca n=0
	         =l1 (+) numerici(l2...ln),daca l1 e numar
		 =numerici(l2...ln),altfel 

vale(l1...ln,d,m,u)=true,daca n=0 si m=1
		   =fals,daca n=0 si m!=1
	           =vale(l2...ln,1,m,l1),daca u>l1 si d=1
	           =vale(l2...ln,1,m+1,l1),daca u>l1 si d!=1
	           =vale(l2...ln,0,m,l1),daca u<l1 si d=0
		   =vale(l2...ln,0,m+1,l1),daca u<l1

main(l1...ln)=true,daca l1>l2 si vale(l2...ln,1,0,l1)=true
	     =false,altfel

|#