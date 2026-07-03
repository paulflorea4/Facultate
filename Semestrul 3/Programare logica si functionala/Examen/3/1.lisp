(defun f(l)
(cond
    ((atom l) -1)
    ((> (f (car l)) 0) (+ (car l) -1 (f (cdr l))))
    (t (f (cdr l)))
)
)