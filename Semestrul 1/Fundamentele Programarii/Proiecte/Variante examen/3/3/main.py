def partition(l,left,right):
    pivot=l[left]
    i=left
    j=right-1
    while i!=j:
        while l[j]>=pivot and i<j:
            j-=1
        l[i]=l[j]
        while l[i]<=pivot and i<j:
            i+=1
        l[j]=l[i]
    l[i]=pivot
    return i

def cerinta1(l):
    """
    pos=partition(l,left,right)
    if left<pos-1:
        cerinta1(l,left,pos-1)
    if pos+1<right:
        cerinta1(l,pos+1,right)
    """
    if len(l)<=1:
        return l
    pivot=l.pop()
    lesser = cerinta1([x for x in l if x < pivot])
    greater = cerinta1([x for x in l if x >=pivot])
    return lesser+[pivot]+greater

def f2(n):
    """
    Functia calculeaza suma numerelor de la 0 la n-1
    :param n:n intreg pentru care se calculeaza suma
    :return:suma numerelor de la 0 la n-1 daca n e strict poztiv
    :raises:ValueError() daca e n <= 0
    """
    if n<=0:raise ValueError()
    l=[x for x in range(n-1,-1,-1)]
    for i in range(n-1):
        l[i+1]+=l[i]
    return l[-1]

def test_f2():
    try:
        f2(0)
        assert False
    except ValueError:
        assert True

    try:
        f2(-4)
        assert False
    except ValueError:
        assert True

    assert f2(2)==1
    assert f2(5)==10

def cerinta3():
    """
    Complexitate timp:
        Best Case:lungimea listei e 1 sau primul element este 0 => theta(1)
        Worst Case:lungimea != 1 si toate elementele sunt nenule
        T(n)=|1 n=1
             |n-1+T(n-1),n>1
        theta(n^2)
    Complexitate spatiu:
        T(n)=|1 n=1
             |T(n-1)+n-1
        theta(n^2)
    """
    pass

def cerinta4(l):
    if l==[]:
        return 0
    if len(l)==1:
        if l[0]>=0:
            return 1
        return 0
    m=len(l)//2
    return cerinta4(l[:m])+cerinta4(l[m:])

def cerinta5(l):
    """
    solutie candidat:x=(x0,x1,...,xk) xi apartine {0,1,2,n-1} (n lungimea listei)
    conditie consistent:x=(x0,x1,...,xk) daca lista[xi] < 0 SAU lista[xi] >=0 pt orice 0<=i<=k
    conditie solutie:k<=n-1 (n lungimea listei)
    """

def main():
    l=[int(x) for x in input().split()]
    print(cerinta4(l))
    #test_f2()

if __name__=='__main__':
    main()