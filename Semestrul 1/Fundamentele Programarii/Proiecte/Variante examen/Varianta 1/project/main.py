def merge_sort(l, st, dr):
    if st >= dr:
        return
    mij = (st + dr) // 2
    merge_sort(l, st, mij)
    merge_sort(l, mij + 1, dr)
    c = []
    i = st
    j = mij + 1
    while i <= mij and j <= dr:
        if l[i] < l[j]:
            c.append(l[i])
            i += 1
        else:
            c.append(l[j])
            j += 1
    while i <= mij:
        c.append(l[i])
        i += 1
    while j <= dr:
        c.append(l[j])
        j += 1
    for i in range(len(c)):
        l[st + i] = c[i]

def cerinta1():
    l = []
    n = int(input())
    line = input()
    for el in line.split():
        l.append(int(el))
    merge_sort(l, 0, len(l) - 1)
    msg = ""
    for el in l:
        msg += str(el) + " "
    print(msg)

def f2(n):
    """
    Functia calculeaza expresia E(n) = 1 + 2 + ... + (n-1) = n * (n-1) / 2
    :param n: numarul pt. care se va calcula expresia
    :type n: int
    :return: Valoarea calculata ( n * (n - 1) / 2 )
    :type return: int
    :raises: ValueError (pentru n <= 0)
    """
    if n <= 0: raise ValueError()
    l = [x for x in range(n-1, -1, -1)]
    for i in range(n-1):
        l[i+1] += l[i]
    return l[-1]

def test_f2():
    try:
        f2(-1)
        assert False
    except ValueError:
        assert True
    try:
        f2(0)
        assert False
    except ValueError:
        assert True
    assert f2(1) == 0
    assert f2(3) == 3
    assert f2(4) == 6

def cerinta3():
    """
    Complexitate timp:

    Best Case : theta(1) (lista are 1 element / primul element este zero)
    Average Case : O(n) (n nr de elemente ale listei)
    Worst Case : theta(n) (toate elementele sunt nenule si lista are lungime n)
        => T(n) = n apartine O(n)

    Complexitate spatiu:

    Best Case : theta(1) (lista are 1 element / primul element este zero)
    Average Case : O(n) (n nr de elemente ale listei)
    Worst Case : theta(n) (toate elementele sunt nenule si lista are lungime n)
        => T(n) = n apartine O(n)
    """

def nr_elem_negative(l):
    if l == []:
        return 0
    if len(l) == 1:
        if l[0] < 0: return 1
        return 0
    m = len(l) // 2
    return nr_elem_negative(l[:m]) + nr_elem_negative(l[m:])

def cerinta4():
    l = []
    n = int(input())
    line = input()
    for el in line.split():
        l.append(int(el))
    print(nr_elem_negative(l))

def cerinta5():
    """
    candidat: (x0, x1, ..., xK), xI apartine lista, K <= N
    consistent: (x0, x1, ..., xK), 0 <= i, j <= k => xI mod 2 == xJ mod 2
    solutie: (x0, x1, ..., xK) consistenta iar k == n-1 (n lungimea listei)
    """

def main():
    #cerinta1()
    #test_f2()
    cerinta4()
    pass

if __name__ == '__main__':
    main()

