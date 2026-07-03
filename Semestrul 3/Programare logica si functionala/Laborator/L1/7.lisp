(defun ultim(l)
	(cond
		((null (cdr l)) (car l))
		(t (ultim (cdr l)))
	)
)

(defun inlocuire(l)
	(cond
		((null l) nil)
		((listp (car l)) (cons (ultim (car l)) (inlocuire (cdr l))))
		(t (cons (car l) (inlocuire (cdr l))))
	)
)

(defun nr_subliste(l)
	(cond
		((null l) 0)
		((atom (car l)) (nr_subliste (cdr l)))
		(t (+ 1 (nr_subliste (car l)) (nr_subliste (cdr l))))	
	)
)

(defun inlocuire_subliste(l)
	(cond
		((= (nr_subliste l) 0) l)
		(t (inlocuire_subliste (inlocuire l)))
	)
)