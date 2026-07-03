def cautare_binara_iterativ(lista,element):
    st=0
    dr=len(lista)-1
    while st<=dr:
        mij=(st+dr)//2
        if element==lista[mij]:
            return mij
        elif element < lista[mij]:
            dr=mij-1
        else:
            st=mij+1
    return -1

def cautare_binara_recursiv(lista,element,st,dr):
    if st>dr:
        return -1
    mij=(st+dr)//2
    if element==lista[mij]:
        return mij
    if element<lista[mij]:
        return cautare_binara_recursiv(lista,element,st,mij-1)
    else:
        return cautare_binara_recursiv(lista,element,mij+1,dr)

def cerinta1():
    l=[int(x) for x in input().split()]
    el=int(input())
    print(cautare_binara_iterativ(l,el))
    print(cautare_binara_recursiv(l,el,0,len(l)-1))

def f(l):
    """
    Functia verifica daca lista data ca parametru este strict crescator
    :param l: lista de numere
    :return: True daca lista e ordonata strict crescator,False altfel
    :raises: ValueError daca lista este vida
    """
    if l==None or l==[]: raise ValueError()
    aux=l[0]-1
    for e in l:
        if aux-e>=0:
            return False
        aux=e
    return True

def test_f():
    try:
        f(None)
        f([])
        assert False
    except ValueError:
        assert True
    assert f([1,2,3])==True
    assert f([1,3,3])==False
    assert f([3,2,1])==False

def cerinta2():
    test_f()

def cerinta3():
    """
    Complexitate timp
        T(n) = 1 if n = len(l) == 0 (sau l == None)
               2 * T(n / 2) + n

        T(n) = 2 * T(n / 2) + n
        T(n / 2) = 2 * T(n / 4) + n / 2
        T(n / 4) = 2 * T(n / 8) + n / 4
        .
        .
        T(2) = 2 * T(1) + 2
        T(1) = 1

        T(n) = 2 * [2 * T(n / 4) + n / 2] + n
             = 2^2 * T(n / 4) + 2 * n
             = 2^2 * [2 * T(n / 8) + n / 4] 2 * n
             = 2^3 * T(n / 8) + 3*n
        k = numarul de injumatatiri ale lui n => n = 2^k
            => k = log2n

        T(n) = 2 ^ (k) * T(1) + (k) * n
        T(n) = (k + 1) * n
        T(n) = log2n * n + n care apartine theta(n * log2n)
    Complexitate spatiu
        -analog timp
    """
    pass

def produs(lista,st,dr):
    if st>dr:
        return 1
    if st==dr:
        if st%2==0:
            return lista[st]
        else:
            return 1
    mij=(st+dr)//2
    return produs(lista,st,mij)*produs(lista,mij+1,dr)

def cerinta4():
    l=[int(x) for x in input().split()]
    print(produs(l,0,len(l)-1))

def cerinta4():
    """
    solutie candidat:x=(x0,x1,...,xM) xI apartine {0,1,2,...,n-1} n=lungimea listei,0<=I<=M
    conditie consistent:x=(x0,x1,...,xM) xI<xJ 0<=i<j<=M suma(xI)<=S
    conditie solutie:x=(x0,x1,...,xM) consistenta si M==k-1
    """

def main():
    #cerinta1()
    #cerinta2()
    cerinta4()

if __name__=="__main__":
    main()