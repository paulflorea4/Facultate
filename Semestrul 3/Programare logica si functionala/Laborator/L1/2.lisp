(defun selecteaza(l n)
	(cond
		((null l) nil)
		((= n 1) (car l))
		(t (selecteaza (cdr l) (- n 1)))
	)
)

(defun membru(l a)
	(cond
		((null l) nil)
		((listp (car l)) (or (membru (car l) a) (membru (cdr l) a)))
		((equal (car l) a) t)
		(t (membru (cdr l) a))
	)
)

(defun subliste(l)
	(cond
		((null l) nil)
		((listp (car l)) (append (list (car l)) (subliste (car l)) (subliste (cdr l)) ))
		(t (subliste (cdr l)))
	)
)

(defun main_subliste(l)
	(cons l (subliste l))
)

(defun transf_multime(l)
	(cond
		((null l) nil)
		((membru (cdr l) (car l)) (transf_multime (cdr l)))
		(t (cons (car l) (transf_multime (cdr l))))
	)	
)