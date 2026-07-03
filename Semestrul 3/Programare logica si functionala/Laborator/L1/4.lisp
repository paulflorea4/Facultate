(defun suma(a b)
	(cond
		((null a) nil)
		(t (cons (+ (car a) (car b)) (suma (cdr a) (cdr b))))
	)
)

(defun atomi(l)
	(cond
		((null l) nil)
		((atom (car l)) (cons (car l) (atomi (cdr l))))
		(t (append (atomi (car l)) (atomi (cdr l))))
	)
)

(defun inverseaza (l)
  (inv-atomi l nil)
)

(defun inv-atomi (l acc)
	(cond
		((null l) acc)
    		((atom (car l)) (inv-atomi (cdr l) (cons (car l) acc)))
    		(t (append acc (list (inverseaza (car l))) (inv-atomi (cdr l) nil)))
	)
)

(defun maxim (l)
	(cond
		((null l) 0)
		((numberp (car l)) (max (car l) (maxim (cdr l))))
		(t (maxim (cdr l)))
	)
)