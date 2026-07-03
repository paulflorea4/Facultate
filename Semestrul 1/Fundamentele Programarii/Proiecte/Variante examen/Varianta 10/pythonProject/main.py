
def partitionare(lista, st, dr):
    poz = st
    for i in range(st, dr):
        if lista[i] < lista[dr]:
            lista[i], lista[poz] = lista[poz], lista[i]
            poz += 1
    lista[dr], lista[poz] = lista[poz], lista[dr]
    return poz

def quick_sort(lista, st, dr):
    if st > dr:
        return
    poz = partitionare(lista, st, dr)
    quick_sort(lista, st, poz-1)
    quick_sort(lista, poz+1, dr)

def minim_lista(lista):
    if lista == []:
        return
    if len(lista) == 1:
        return lista[0]
    mij = len(lista) // 2
    return min(minim_lista(lista[:mij]), minim_lista(lista[mij:]))

def main():
    #input()
    lista = [int(x) for x in input().split()]
    print(minim_lista(lista))
    #quick_sort(lista, 0, len(lista) - 1)
    #txt = ""
    #for i in lista:
        #txt += str(i) + " "
    #print(txt)

if __name__ == '__main__':
    main()