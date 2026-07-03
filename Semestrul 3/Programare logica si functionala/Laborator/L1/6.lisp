(defun dublu(l n)
	(cond
		((null l) nil)
		((= n 1) (cons (car l) (dublu l (- n 1))))
		(t (cons (car l) (dublu (cdr l) (- n 1))))
	)
)

(defun asociere(a b)
	(cond
		((null a) nil)
		(t (cons (cons (car a) (car b)) (asociere (cdr a) (cdr b) ) ) )
	)
)

(defun nrSubliste(l)
	(cond
		((null l) 0)
		((atom (car l)) (nrSubliste (cdr l)))
		(t (+ 1 (nrSubliste (car l)) (nrSubliste (cdr l))))
	)
)

(defun mainNrSubliste(l)
	(+ 1 (nrSubliste l)))
