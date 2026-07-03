
def cerinta1(lista):
    for i in range(1, len(lista)):
        j = i-1
        copie = lista[i]
        while j >= 0 and lista[j] > copie:
            lista[j+1] = lista[j]
            j -= 1
        lista[j+1] = copie
    txt = ""
    for el in lista:
        txt += str(el) + ' '
    print(txt)

def cerinta3():
    """
    Complexitate timp
    Best case: theta(1) (lista x e goala)
    Worst case: theta(n * log2n) (toate elementele sunt neg) (n lungimea listei)
    Average case: O(n * log2n)

    Complexitate spatiu
    Best case: theta(1) (lista x e goala)
    Worst case: theta(n * log2n) (toate elementele sunt neg) (n lungimea listei)
        -la fiecare pas este adaugat cate 1 element in lista l
    Average case: O(n * log2n)
        -la fiecare pas este adaugat cate 1 element in lista l
    """

def cerinta4(lista):
    if lista == []:
        return lista
    if len(lista) == 1:
        if lista[0] % 2 == 1: return lista
        return []
    mij = len(lista) // 2
    return cerinta4(lista[:mij]) + cerinta4(lista[mij:])

def sclm_rec():


def main():
    #input()
    lista = [int(x) for x in input().split()]
    #cerinta1(lista)
    #print(cerinta4(lista))
    sclm_rec()

if __name__ == '__main__':
    main()
