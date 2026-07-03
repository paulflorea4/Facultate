(defun or-list (l)
    (cond
        ((null l) nil)
        (t (or (car l) (or-list (cdr l))))
    )
)

(defun membru (a l)
    (cond
        ((null l) nil)
        ((atom l) (eql a l))
        (t (or-list (mapcar (lambda (x) (membru a x)) l)))
    )
)

#|
or-list(l1...ln) = false , n = 0
                 = true , daca l1 = true
                 = or-list(l2...ln) , altfel

membru(a,l1...ln) = false , n = 0
                  = true , l1 e atom si l1 = a
                  = or-list( U membru(a,li) ) , altfel

(membru 'a '(b (c d) (e (x f)))) 

(membru 3 '(1 (2 (3 4)) 5))

(membru 'x '(b (c d) (e (a f))))

(membru 'x '(3 b (c d) (x (1 f) e)))
|#
