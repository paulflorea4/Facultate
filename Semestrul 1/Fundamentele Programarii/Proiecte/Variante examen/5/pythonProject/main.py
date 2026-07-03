
def partitionare(lista,st,dr):
    poz=st
    for i in range(st,dr):
        if lista[i]<lista[dr]:
            lista[i],lista[poz]=lista[poz],lista[i]
            poz+=1
    lista[poz],lista[dr]=lista[dr],lista[poz]
    return poz

def quickSort(lista,st,dr):
    if st>=dr:
        return lista
    poz=partitionare(lista,st,dr)
    quickSort(lista,st,poz-1)
    quickSort(lista,poz+1,dr)

def cerinta1():
    l=[int(x) for x in input().split()]
    quickSort(l,0,len(l)-1)
    print(l)

def f(n):
    """
    Functia calculeaza suma cifrelor numarului dat ca parametru daca acesta este strict pozitiv
    :param n: numar intreg pentru care se calculeaza suma cifrelor
    :return: suma cifrelor
    :raises: ValueError daca n<=0
    """
    if n<=0: raise ValueError()
    l=[]
    while n>0:
        c=n%10
        n=n//10
        l.append(c)
    for i in range(len(l)-1): l[i+1]+=l[i]
    return l[-1]

def test_f():
    try:
        f(-2)
        assert False
    except ValueError:
        assert True

    assert f(1)==1
    assert f(13)==4
    assert f(123)==6
    assert f(1023)==6

def cerinta2():
    test_f()

def cerinta3():
    """
    Complexitate timp/Complexitate spatiu
    BC=WC=Ac
    while-ul se executa de log2n pasi
    for-ul se executa de n*n pasi
    T(n)=n*n*log2n apartine theta(n*n*log2n)
    """
    pass

def inversare(lista):
    if lista==[]:
        return []
    if len(lista)==1:
        return lista
    mij=len(lista)//2
    return inversare(lista[mij:])+inversare(lista[:mij])

def cerinta4():
    l=[int(x) for x in input().split()]
    print(inversare(l))
    print(l)

def secv_pare_desc(lista):
    n=len(lista)
    r=[0]*n
    p=[-1]*n
    if lista[-1]%2==0:
        r[-1]=1
    for i in range(n-2,-1,-1):
        pmax=i
        if lista[i]%2==0:
            for j in range(i+1,n):
                if lista[j]%2==0 and lista[i]>lista[j] and r[j]>r[pmax]:
                    pmax=j
                    p[i]=j
            r[i]=r[pmax]+1

    pmax=0
    for i in range(1,n):
        if r[i]>r[pmax]:
            pmax=i

    rez=[]
    while pmax!=-1:
        rez.append(lista[pmax])
        pmax=p[pmax]
    print(rez)

def cerinta5():
    lista = [int(x) for x in input().split()]
    secv_pare_desc(lista)

def main():
    #cerinta1()
    #cerinta2()
    #cerinta4()
    cerinta5()

if __name__=="__main__":
    main()