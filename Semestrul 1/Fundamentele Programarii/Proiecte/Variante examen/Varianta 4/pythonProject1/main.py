

def cerinta1():
    #cautare_binara()
    l = [int(x) for x in input().split()]
    el = int(input())
    print(cautare_binara_recursiv(l, el, 0, len(l)))
    pass

def f(n):
    """
    Functia verifica daca numarul strict pozitiv transmis
    ca parametru contine cel putin o cifra para
    :param n: numarul pentru care se verifica proprietatea (n > 0)
    :type n: int
    :return: True (daca verifica conditia) / False (in caz contrar)
    :return type: Boolean
    :raises: ValueError (daca n <= 0)
    """
    if n <= 0: raise ValueError()
    while n > 0:
        c = n % 10
        n = n // 10
        if c % 2 == 0: return True
    return False

def test_f():
    try:
        f(0)
        assert False
    except ValueError:
        assert True
    assert f(1153) == False
    assert f(1125) == True

def cerinta3():
    """
    Complexitate timp
    Best Case: theta(1) (lista este goala)
    Worst Case: theta(2^n) (n lungimea listei)
    Average Case: theta(2^n) (n lungimea listei)
        => T(n) = 2^n care apartine theta(2^n)

    Complexitate spatiu
    Best Case = Worst Case = Average Case = theta(1)
    (folosim decat un numar constant de variabile auxiliare)
    """

def cerinta4(lista):
    if lista == []:
        return False
    if len(lista) == 1:
        if lista[0] % 2 == 0: return True
        return False
    mijloc = len(lista) // 2
    return cerinta4(lista[:mijloc]) or cerinta4(lista[mijloc:])

def prim(n):
    if n <= 1:
        return False
    if n % 2 == 0 and n > 2:
        return False
    for i in range(3, n):
        if n % i == 0:
            return False
    return True

def lmsdnmi(lista):
    r = [0] * len(lista)
    p = [-1] * len(lista)
    if(prim(lista[-1])):
        r[-1] = 1
    n = len(lista)
    for i in range(n-2, -1, -1):
        pmax = i
        if prim(lista[i]):
            for j in range(i+1, n):
                if (prim(lista[j]) and lista[j] > lista[i] and
                        r[pmax] < r[j]):
                    pmax = j
                    p[i] = j
            r[i] = 1 + r[pmax]
    ind_curent = 0
    for i in range(1, n):
        if r[i] > r[ind_curent]:
            ind_curent = i
    txt = ""
    while ind_curent != -1:
        txt += str(lista[ind_curent]) + " "
        ind_curent = p[ind_curent]
    print(txt)

def cerinta5():
    lista = [int(x) for x in input().split()]
    lmsdnmi(lista)

def main():
    cerinta1()
    #cerinta5()

if __name__ == '__main__':
    main()

