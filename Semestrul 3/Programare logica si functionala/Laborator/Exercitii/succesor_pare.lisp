(defun succesor_pare(l)
    (cond
        ((and (numberp l) (evenp l)) (+ l 1))
        ((atom l) l)
        (t (mapcar #'succesor_pare l))
    )
)

(defun nivel_k(arb k)
    (cond
        ((and (atom arb) (= k 0)) (list arb))
        (t (mapcan #'(lambda (l) (nivel_k l (- k 1))) arb))
    )
)

(defun main_nivel_k(arb k)
    (nivel_k arb (+ k 1))
)

#|
nivel_k(l1...ln,k)=[l11],daca l1 e lista si k=0
                  =[l1],daca l1 e atom si k=0
                  =U nivel_k(li,k-1),altfel
|#

(defun nivel_impar(arb e nivel)
    (cond
        ((and (atom arb) (oddp nivel)) e)
        ((and (atom arb) (evenp nivel)) arb)
        (t (mapcar #'(lambda (l) (nivel_impar l e (+ nivel 1))) arb))
    )
)

(defun main_nivel_impar(arb e)
    (nivel_impar arb e -1)
)

(defun elim (l nivel k)
(cond
    ((and (atom l) (= nivel k)) nil)
    ((atom l) l)
    (t (mapcar #'(lambda(l) (elim l (+ nivel 1) k)) l))
)
)