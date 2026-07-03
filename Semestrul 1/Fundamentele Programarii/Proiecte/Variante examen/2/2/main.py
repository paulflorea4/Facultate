def cerinta1(lst):
    for i in range(len(lst)-1):
        for j in range(i+1,len(lst)):
            if lst[i]>lst[j]:
                lst[i],lst[j] = lst[j],lst[i]

    for i in range(len(lst)-1):
        ind=i
        for j in range(i+1,len(lst)):
            if lst[ind]>lst[j]:
                ind=j
        if i<ind:
            lst[i],lst[ind]=lst[ind],lst[i]

    return lst

def f(n):
    """
    Functia returneaza al n-lea termen din sirul lui Fibonacci
    :param n: numar intreg reprezentand al n-lea termen
    :return: termenul de pe pozitia n daca n este pozitiv
    :raises: ValueError daca n este strict negativ
    """
    if n<0: raise ValueError()
    if n<=1:return n
    l=[0]*(n+1)
    l[1]=1
    for i in range(2,n+1):
        l[i]=l[i-1]+l[i-2]
    return l[n]

def test_f():
    try:
        f(-1)
        assert False
    except ValueError:
        assert True
    assert f(0)==0
    assert f(1)==1
    assert f(2)==1
    assert f(5)==5

def cerinta3():
    """
        Complexitate timp:
            Best Case=Worst Case=Average Case apartine theta(n^4log2n)
        Complexitate spatiu:
            theta(1)
    """
    pass

def cerinta4(lst):
    if lst==[]:
        return
    if len(lst)==1:
        return lst[0]
    m=len(lst)//2
    return max(cerinta4(lst[:m]),cerinta4(lst[m:]))

def cerinta5():
    """
    solutie candidat:
        x=(x0,x1,x2,...,xK) xI = { '(',')','{','}'}
    conditie consistent:
        -nr de paranteze(acolade) deschise pana la pozitia i este >= nr de paranteze(acolade) inchise
        pana la pozitia i
        -nr de pozitii libere ramase pana la solutie
        trebuie sa fie >= nr de paranteze(acolade) deshise - nr de paranteze(acolade) inchise
    conditie solutie:
        x=(x0,x1,x2,...,xk) daca e consistent si k==n-1
    """

def main():
    #print(cerinta1([int(x) for x in input().split()]))
    #test_f()
    print(cerinta4([int(x) for x in input().split()]))

if __name__ == '__main__':
    main()