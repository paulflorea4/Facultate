(defun interclasare(a b)
	(cond
		((null a) b)
		((null b) a)
		((<= (car a) (car b)) (cons (car a) (interclasare (cdr a) b)))
		(t (cons (car b) (interclasare a (cdr b))))
	)
)

(defun substituie(l e l1)
	(cond
		((null l) nil)
		((equal (car l) e) (cons l1 (substituie (cdr l) e l1)))
		((atom (car l)) (cons (car l) (substituie (cdr l) e l1)))
		(t (cons (substituie (car l) e l1) (substituie (cdr l) e l1)))
	)
)

(defun transformare(l)
	(cond
		((null l) 0)
		(t (+ (* (expt 10 (- (length l) 1)) (car l)) (transformare (cdr l))))
	)
)

(defun suma(a b)
	(+ (transformare a) (transformare b))
)