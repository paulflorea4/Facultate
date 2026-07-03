#| a.
insereaza(l1...lm,n,e) = e (+) l1...lm , daca n=0
		       = [e] , daca m=0
                       = l1 (+) insereaza(l2...lm,n-1,e) , altfel

(insereaza '8 '3 '(1 2 3 4 5))
(insereaza '9 '0 '(1 2 3 4 5))
(insereaza '9 '7 '(1 2 3 4 5))
|#

(defun insereaza (e n l)
  (cond
    ((= n 0) (cons e l))
    ((null l) (list e))
    (t (cons (car l) (insereaza e (- n 1) (cdr l))))
  )
)

#| b.
suma(l1...ln) = 0 , daca n=0
              = l1 + suma(l2...ln) , daca l1 e numar
              = suma(l2...ln) , daca l1 e atom
              = suma(l1) + suma(l2...ln) , daca l1 e lista

(suma '((1 (a 3) 2 (3 4) b 1) a 3))
(suma '(2 (x 1) (3 (3 1) b 0) a 3))
(suma '((4 ((a 1) 0) (3 2) b 3) (b 1)))
|#

(defun suma(l)
  (cond
 	((null l) 0)
 	((numberp (car l)) (+ (car l) (suma (cdr l))))
 	((atom (car l)) (suma (cdr l)))
 	(t (+ (suma (car l)) (suma (cdr l))))
 )
) 

#| c.
sublista(l1...ln) = [] , daca n=0
                  = sublista(l2...ln) , daca l1 e atom
                  = sublista(l1) (+) l1 (+) sublista(l2...ln) , daca l1 e lista

(sublista '((1 2 3) (4 5 (9 8 7) 6)))
(sublista '((1 2 3) ((4 5) 6)))
(sublista '((1 2 3) (4 5 (9 8 7) 6 (10 11))))
|#

(defun sublista(l)
  (cond
	((null l) nil)
	((atom (car l)) (sublista(cdr l)))
	(t (append (sublista (car l)) (list (car l)) (sublista (cdr l))))
  )
)

#| d.
membru(l1...ln,e) = false , n=0
                  = true , daca l1=e
                  = membru(l2...ln,e) , altfel

inclus(a1...an,b1...bm) = true , daca n=0
                        = inclus(a2...an,b1...bm), membru(b1...bm,a1)=true
                        = false, altfel

egale(a1...an,b1...bm) = true , inclus(a1...an,b1...bm)=true si inclus(b1...bm,a1...an)=true
                       = false , altfel

(egale '() '())
(egale '(1 2 3) '(3 2 1))
(egale '(1 2 3) '(1 2 3 4))
(egale '(a b c) '(c b a))
|#

(defun membru (e l)
  (cond
  	((null l) nil)
	((equal e (car l)) t)
	(t (membru e (cdr l)))
  )
)

(defun inclus (m1 m2)
  (cond
	((null m1) t)
        ((membru (car m1) m2) (inclus (cdr m1) m2))
        (t nil)
  )
)

(defun egale (m1 m2)
  (and (inclus m1 m2) (inclus m2 m1))
)