

def insertion_sort(lista):
    for i in range(1, len(lista)):
        index = i-1
        copie = lista[i]
        while index >= 0 and copie < lista[index]:
            lista[index + 1] = lista[index]
            index -= 1
        lista[index + 1] = copie

def cerinta1():
    input()
    lista = [int(x) for x in input().split()]
    insertion_sort(lista)
    text = ""
    for x in lista:
        text += str(x) + ' '
    print(text)

def f2(n):
    """acelasi lucru ca la var 1"""
    if n <= 0: raise ValueError()
    l = [x for x in range(n-1, -1, -1)]
    for i in range(n-1):
        l[i+1] += l[i]
    return l[-1]

def cerinta3():
    """
    Complexitate timp

    Complexitate spatiu

    """
def cnt_nr_poz(lista):
    if lista == []:
        return 0
    if len(lista) == 1:
        if lista[0] > 0: return 1
        return 0
    mijloc = len(lista) // 2
    return (cnt_nr_poz(lista[:mijloc])
            + cnt_nr_poz(lista[mijloc:]))

def cerinta4():
    input()
    lista = [int(x) for x in input().split()]
    print(cnt_nr_poz(lista))

def cerinta5():
    """
    candidat: (x0, x1, ..., xK) K <= N (N lungimea listei)
        xI apartine {0, .., K}
        xI != xJ oricare 0 <= i, j <= K
    consistent: (x0, x1, ..., xK) K <= N (N lungimea listei)
        pentru i apartine {0, .., k} lista[xI] >= 0 SAU
        pentru i apartine {0, .., K} lista[xI] < 0
    solutie: (x0, x1, ..., xK) K <= N (lungimea listei)
        consistenta
    """

def main():
    #cerinta1()
    #cerinta4()

if __name__ == '__main__':
    main()
