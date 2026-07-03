(defun inordine(l)
	(cond
		((null l) NIL)
		(t (append (inordine (cadr l)) (list (car l)) (inordine (caddr l))))
	)
)