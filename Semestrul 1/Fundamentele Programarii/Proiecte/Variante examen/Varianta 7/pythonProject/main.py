def cerinta3():
    """
    Complexitate timp
    While-ul din interior se executa de log2n ori
    For-ul din exterior se executa de n*n ori
    i = 1 => log2n
    i = 2 => log2n
    ...
    i = n*n => log2n
    T(n) = log2n * n * n care apartine theta(log2n * n * n)

    Complexitate spatiu
    In while-ul din interior, la fiecare pas se adauga
    cate 1 element, adica dupa un while, adaugam in lista
    log2n elemente. While-urile se executa de n*n ori
    T(n) = log2n * n * n care apartine theta(log2n * n * n)
    """

def cerinta5():
    """
    Solutiile sunt formate din indicii elementelor din lista, nu
    de elementele propriu-zise
    candidat: (x0, x1, ..., xM) 0 <= xI <= N-1 (N lungimea listei)
    consistent: (x0, x1, ..., xM) suma(lista[xI]) <= S
                            si xI != xJ oricare (0 <= i, j <= M)
    solutie: (x0, x1, ..., xM) consistenta si M == K-1
    """

def main():
    cerinta3()

if __name__ == '__main__':
    print_hi('PyCharm')
