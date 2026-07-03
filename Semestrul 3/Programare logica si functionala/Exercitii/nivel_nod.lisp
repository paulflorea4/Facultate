(defun nivel_nod(l n nivel)
	(cond
		((null l) -1)
		((equal (car l) n) nivel)
		(t (max (nivel_nod (cadr l) n (+ nivel 1)) (nivel_nod (caddr l) n (+ nivel 1))))
	)
)