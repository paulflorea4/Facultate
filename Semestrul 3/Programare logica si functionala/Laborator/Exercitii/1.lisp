(defun insereaza(l a nr)
    (cond
        ((= nr 2) (append (list a) (insereaza l a 0)))
        ((null l) nil)
        (t (cons (car l) (insereaza (cdr l) a (+ nr 1))))
    )
)

(defun extrage(l)
    (cond
        ((null l) nil)
        ((atom (car l)) (append (extrage (cdr l)) (list (car l))))
        (t (append (extrage (cdr l)) (extrage (car l))))
    )
)

(defun sublista(l)
  (cond
	((null l) nil)
	((atom (car l)) (sublista(cdr l)))
	(t (append (list (car l)) (sublista (car l)) (sublista (cdr l))))
  )
)

;; (defun adancime(l crt)
;;     (cond
;;         ((null l) crt)
;;         ((atom (car l)) (adancime (cdr l) crt))
;;         (t (max (adancime (car l) (+ crt 1)) (adancime (cdr l) crt)))
;;     )
;; )

(defun adancime (l)
  (cond
    ((null l) 0)
    ((atom l) 0) 
    (t (max (+ 1 (adancime (car l ) ) )
            (adancime (cdr l)))
    )
  )
)

(defun inverseaza (l)
  (inv-atomi l nil))

(defun inv-atomi (l acc)
  (cond
    ((null l) acc)

    ((atom (car l))
     (inv-atomi (cdr l) (cons (car l) acc)))

    (t
     (append acc
             (list (inverseaza (car l)))
             (inv-atomi (cdr l) nil)))))

