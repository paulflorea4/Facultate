(defun parcurg_st (arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) nil)
    (t (cons (car arb)
             (cons (cadr arb)
                   (parcurg_st (cddr arb)
                               (+ nv 1)
                               (+ nm (cadr arb))))))))
(defun parcurg_d (arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) arb)
    (t (parcurg_d (cddr arb)
                  (+ nv 1)
                  (+ nm (cadr arb))))))

(defun stang (arb)
  (parcurg_st (cddr arb) 0 0))

(defun drept (arb)
  (parcurg_d (cddr arb) 0 0))

(defun niveluri (arb)
  (cond
    ((null arb) 0)
    (t (+ 1 (max (niveluri (stang arb))
                 (niveluri (drept arb)))))
  )
)

(defun nr (arb)
  (cond
    ((null arb) 0)
    ((and (null (drept arb)) (null (stang arb))) 0)
    ((null (drept arb) ) (nr (stang arb)))
    (t (+ 1 (nr (stang arb)) (nr (drept arb))))
)
)

#|
parcurg_st(l1...lk,nv,nm)=[], daca k=0
                            =[], daca nv=1+nm
                            =l1(+)l2(+)parcurg_st(l3...lk,nv+1,nm+l2) altfel

stang(l1...ln)=parcurg_st(l3...ln,0,0)

parcurg_d(l1...lk,nv,nm)=[],daca k=0
                            =(l1,l2...lk), daca nv=1+nm
                            =parcurg_d(l3...lk,nv+1,nm+l2)

drept(l1...ln)=parcurg_d(l3...ln,0,0)

niveluri(l1...ln)=0, daca n=0
                 =1+max(niveluri(stang(l1...ln)),drept(l1...ln))

(niveluri '(a 1 b 1 c 0))
(niveluri '(a 2 b 2 c 1 i 0 f 1 g 0 d 2 e 0 h 0))
(niveluri '(A 2 B 0 C 2 D 0 E 0))
|#

