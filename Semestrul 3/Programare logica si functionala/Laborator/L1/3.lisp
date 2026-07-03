(defun produs(a b)
	(cond
		((null a) 0)
		(t (+ (produs (cdr a) (cdr b)) (* (car a) (car b))))
	)
)

(defun adancime(l)
	(cond
		((null l) 0)
		((atom l) 0)
		(t (max (+ 1 (adancime (car l))) (adancime (cdr l))))
	)
)

(defun insereaza(l e)
	(cond
		((null l) (list e))
		((= e (car l)) l)
		((< e (car l)) (cons e l))
		(t (cons (car l) (insereaza (cdr l) e)))
	)
)

(defun sortare_fara_dubluri(l)
	(cond
		((null l) nil)
		(t (insereaza (sortare_fara_dubluri (cdr l)) (car l)))
	)
)

(defun membru(l e)
	(cond
		((null l) nil)
		((equal e (car l)) t)
		(t (membru (cdr l) e))
	)
)

(defun intersectie(a b)
	(cond
		((null a) nil)
		((membru b (car a)) (cons (car a) (intersectie (cdr a) b)))
		(t (intersectie (cdr a) b))
	)
)