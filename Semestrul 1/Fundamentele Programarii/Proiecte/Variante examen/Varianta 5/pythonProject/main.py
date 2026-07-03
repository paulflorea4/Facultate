def partitionare(lista, st, dr):
    poz = st
    for i in range(st, dr):
        if lista[i] < lista[dr]:
            lista[i], lista[poz] = lista[poz], lista[i]
            poz += 1
    lista[dr], lista[poz] = lista[poz], lista[dr]
    return poz

def quick_sort1(lista, st, dr):
    if st >= dr:
        return lista
    poz = partitionare(lista, st, dr)
    quick_sort1(lista, st, poz-1)
    quick_sort1(lista, poz+1, dr)

def quick_sort(arr):
    """
    Quicksort using list comprehensions
    Returns a new sorted list
    """
    if len(arr) <= 1:
        return arr
    pivot = arr[-1]  # Select the pivot without mutating the list
    lesser = quick_sort([x for x in arr[:-1] if x < pivot])  # Exclude the pivot from comparison
    greater = quick_sort([x for x in arr[:-1] if x >= pivot])
    return lesser + [pivot] + greater



def cerinta1():
    input()
    lista = [int(x) for x in input().split()]
    lista = quick_sort(lista)
    msg = ""
    for x in lista:
        msg += str(x) + " "
    print(msg)

def cerinta3():
    """
    Complexitate timp
    While-ul executa log2n pasi
    For-ul executa n * n pasi
    Best case = Worst case = Average Case
        T(n) = n * n * log2n care apartine Theta(n * n * log2n)

    Complexitate spatiu

    """
def cerinta4(lista):
    if lista == []:
        return
    if len(lista) == 1:
        return lista
    mijloc = len(lista) // 2
    return  cerinta4(lista[mijloc:]) + cerinta4(lista[:mijloc])

def sclm(lista):
    n = len(lista)
    r = [0] * n
    p = [-1] * n
    if lista[-1] % 2 == 0:
        r[-1] = 1
    for i in range(n-2, -1, -1):
        pmax = i
        if lista[i] % 2 == 0:
            for j in range(i+1, n):
                if (lista[j] % 2 == 0 and
                        lista[j] < lista[i] and
                        r[j] > r[pmax]):
                    pmax = j
                    p[i] = j
            r[i] = r[pmax] + 1
    pmax = 0
    for i in range(1, n):
        if r[i] > r[pmax]:
            pmax = i
    text = ""
    while pmax != -1:
        text += str(lista[pmax]) + ' '
        pmax = p[pmax]
    print(text)

def cerinta5():
    lista = [int(x) for x in input().split()]
    sclm(lista)

def main():
    #cerinta1()
    #print(cerinta4([int(x) for x in input().split()]))
    cerinta5()

if __name__ == '__main__':
    main()
