def cautare_binara(lista,element,st,dr):
    st=0
    dr=len(lista)-1
    while st<=dr:
        mij=(st+dr)//2
        if lista[mij]==element:
            return mij
        elif element<lista[mij]:
            dr=mij-1
        else:
            st=mij+1
    return -1

def cautare_binara_rec(lista,element,st,dr):
    if st>dr:
        return -1
    mij=(st+dr)//2
    if lista[mij]==element:
        return mij
    elif element<lista[mij]:
        return cautare_binara_rec(lista,element,st,mij-1)
    else:
        return cautare_binara_rec(lista,element,mij+1,dr)

def cerinta1():
    l=[int(x) for x in input().split()]
    el=int(input())
    print(cautare_binara(l,el,0,len(l)))

def f(n):
    """
    Functia verifica daca numarul dat ca parametru are cel putin o cifra para
    :param n: numarul intreg de verificat
    :return: True daca n contine cel putin o cifra para,False altfel
    :raises: ValueError daca numarul este <=0
    """
    if (n<=0): raise ValueError()

    while n>0:
        c=n%10
        n=n//10
        if (c%2==0): return True
    return False

def test_f():
    try:
        f(0)
        assert False
    except ValueError:
        assert True
    assert f(11351)==False
    assert f(123)==True
    assert f(4)==True
    assert f(7)==False

def cerinta2():
    test_f()

def cerinta3():
    """
    Complexitate timp:
    BC:lista e goala => theta(1)
    WC:theta(2^n)(n lungimea listei)
    AC:theta(2^n)

    Complexitate spatiu:
    BC=WC=AC=theta(1)
    folosim un nr constant de variabile auxiliare

    """
    pass

def cerinta4(lista):
    if lista==[]:
        return False
    if len(lista)==1:
        if lista[0]%2==0:
            return True
        return False
    mij=len(lista)//2
    return cerinta4(lista[:mij]) or cerinta4(lista[mij:])

def prim(n):
    if n<=1:
        return False
    if n%2==0 and n>2:
        return False
    for i in range(3,n):
        if n%i==0:
            return False
    return True

def cerinta5(lista):
    n = len(lista)
    r=[0]*n
    p=[-1]*n
    if prim(lista[-1]):
        r[-1]=1
    for i in range(n-2,-1,-1):
        pmax=i
        if prim(lista[i]):
            for j in range(i+1,n):
                if (prim(lista[j]) and lista[j]>lista[i] and
                    r[pmax]<r[j]):
                    pmax=j
                    p[i]=j
            r[i]=1+r[pmax]

    ind_curent=0
    for i in range(1,n):
        if r[i]>r[ind_curent]:
            ind_curent=i

    txt=""
    while ind_curent!=-1:
        txt+=str(lista[ind_curent])+" "
        ind_curent=p[ind_curent]
    print(txt)

def main():
    #cerinta1()
    #cerinta2()
    l = [int(x) for x in input().split()]
    cerinta5(l)

if __name__ == "__main__":
    main()