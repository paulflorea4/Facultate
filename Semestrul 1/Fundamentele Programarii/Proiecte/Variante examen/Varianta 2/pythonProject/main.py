import datetime


def selection_sort(lista):
    for i in range(len(lista) - 1):
        for j in range(i+1, len(lista)):
            if lista[j] < lista[i]:
                lista[i], lista[j] = lista[j], lista[i]

def cerinta1():
    input()
    lista = [int(x) for x in input().split()]
    selection_sort(lista)
    msg = ""
    for x in lista:
        msg += str(x) + ' '
    print(msg)

def f(n):
    """
    Functia calculeaza sirul lui fibonacci pornind
    de la indicele 0 pana la indicele n
    :param n: indicele pana la care se calculeaza termenii
    lui fibonacci
    :type n: int
    :return: al n-lea termen al lui fibonacci
    :return type: int
    :raises: ValueError (daca n < 0)
    """
    if n < 0: raise ValueError()
    if n <= 1: return n
    l = [0] * (n + 1)
    l[1] = 1
    for i in range(2, n + 1):
        l[i] = l[i - 1] + l[i - 2]
    return l[n]

def test_f():
    try:
        f(-5)
        assert False
    except ValueError:
        assert True
    assert f(0) == 0
    assert f(1) == 1
    assert f(2) == 1
    assert f(3) == 2
    assert f(4) == 3

def cerinta3():
    """
    Complexitate timp

    While-ul se executa de log2n ori pt ca k porneste
        de la 1 si se dubleaza la fiecare pas
    For-ul se executa de n * n ori
    => T(n) = n * n * log2n care apartine theta(n * n * log2n)

    Complexitate spatiu - theta(1)
    (sunt declarate un numar constant de variabile auxiliare)
    """

def determinare_maxim(lista):
    if lista == []:
        return
    if len(lista) == 1:
        return lista[0]
    return max(determinare_maxim(lista[:len(lista) // 2]),
               determinare_maxim(lista[len(lista) // 2:]))

def cerinta4():
    lista = [int(x) for x in input().split()]
    print(determinare_maxim(lista))

def cerinta5():
    """
    candidat: (x0, x1, ..., xK) xI = '{' / '}' / '(' / ')'
    consistent: (x0, x1, ..., xK)
        -numarul de paranteze (acolade) deschise pana la pozitia i
        este >= numarul de paranteze (acolade) inchise pana la pozitia i
        -numarul de pozitii libere ramase pana la solutie (N - K - 1) trebuie
        sa fie >= numarul de paranteze (acolade) deschise - numarul de
        paranteze (acolade) inchise
    solutie: (x0, x1, ..., xK) consistenta si K = N - 1
    """

def main():
    #cerinta1()
    #test_f()
    #cerinta4()

    print(datetime.datetime(2020, 10, 5).year)

if __name__ == '__main__':
    main()
