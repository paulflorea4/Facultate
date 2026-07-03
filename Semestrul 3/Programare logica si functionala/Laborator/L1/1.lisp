(defun insereaza(l a nr)
	(cond
		((null l) (if (= nr 2) (list a) nil))
		((= nr 2) (cons a (insereaza l a 0)))
		(t (cons (car l) (insereaza (cdr l) a (+ nr 1))))
	)
)

(defun atomi(l)
	(cond
		((null l) nil)
		( (atom (car l)) ( append (atomi (cdr l)) (list (car l)) ) )
		(t (append (atomi (cdr l)) (atomi (car l))))
	)
)

(defun cmmdc(a b)
	(cond
		((= a 0) b)
		((= b 0) a)
		((= a b) a)
		((> a b) (cmmdc (- a b) b))
		(t (cmmdc a (- b a)))
	)
)

(defun cmmdc_lista(l)
	(cond
		((null l) 0)
		((numberp (car l)) (cmmdc (car l) (cmmdc_lista (cdr l))))
		((listp (car l)) (cmmdc (cmmdc_lista (car l)) (cmmdc_lista (cdr l))))
		(t (cmmdc_lista (cdr l)))
	)
)

(defun aparitii(l a)
	(cond
		((null l) 0)
		((and (atom (car l)) (eql (car l) a)) (+ 1 (aparitii (cdr l) a)))
		((atom (car l)) (aparitii (cdr l) a))
		(t (+ (aparitii (car l) a) (aparitii (cdr l) a)))
	)
)