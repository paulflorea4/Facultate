
def cautare_binara_recursiv(lista, st, dr, element):
    if st > dr:
        return -1
    mij = (st + dr) // 2
    if lista[mij] == element:
        return mij
    elif lista[mij] > element:
        return (
            cautare_binara_recursiv(lista, st, mij-1, element))
    else:
        return (
            cautare_binara_recursiv(lista, mij+1, dr, element))

def cautare_binara_nerecursiv(lista, element):
    st, dr = 0, len(lista) - 1
    while st <= dr:
        mij = (st + dr) // 2
        if lista[mij] == element:
            return mij
        if lista[mij] < element:
            st = mij + 1
        else: dr = mij-1
    return -1

def cerinta1():
    lista = [int(x) for x in input().split()]
    n = int(input())
    #print(cautare_binara_recursiv(lista, 0, len(lista) - 1, n))
    #print(cautare_binara_nerecursiv(lista, n))

def f(l):
    """
    Functia verifica daca o lista de numere este ordonata strict crescator
    :param l: lista pentru care verificam proprietatea
    :type l: list
    :return: True (daca lista respecta conditia) / False (in caz contrar)
    :return type: Boolean
    :raises: ValueError - daca lista este goala sau este None
    """
    if l == None or l == []: raise ValueError()
    aux = l[0] - 1
    for e in l:
        if (aux - e >= 0):
            return False
        aux = e
    return True

def test_f():
    """
    Functia realizeaza un set de teste pentru functia f
    """
    try:
        f(None)
        assert False
    except ValueError:
        assert True
    try:
        f([])
        assert False
    except ValueError:
        assert True
    assert f([1, 1, 2, 3]) == False
    assert f([1, 2, 3, 4]) == True
    assert f([4, 3, 2, 1]) == False

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

def cerinta4(lista, st, dr):
    if lista == []:
        return 0
    if st > dr:
        return 1
    if st == dr:
        if st % 2 == 0: return lista[st]
        return 1
    mij = (st + dr) // 2
    return cerinta4(lista, st, mij) * cerinta4(lista, mij+1 ,dr)

def backtracking(lista, lista_sol):


def cerinta5():
    """
    candidat: (a0, a1, ..., aM) aI apartine listei de nr (0 <= I <= M)
    consistenta: (a0, a1, ..., aM) a0 + a1 + ... + aM <= S
    solutie: (a0, a1, ..., aM) consistenta si M == K - 1
    """
    lista = [int(x) for x in input().split()]
    backtracking(lista, [])

def main():
    """"""
    #cerinta1()
    #test_f()
    #lista = [int(x) for x in input().split()]
    #print(cerinta4(lista, 0, len(lista) - 1))

if __name__ == '__main__':
    main()
