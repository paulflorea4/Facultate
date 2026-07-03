def partitionare(lista,st,dr):
    poz=st
    for i in range(st,dr):
        if lista[i]<lista[dr]:
            lista[i],lista[poz]=lista[poz],lista[i]
            poz+=1
    lista[poz],lista[dr]=lista[dr],lista[poz]
    return poz

def quickSort(lista,st,dr):
    if st>=dr:
        return lista
    poz=partitionare(lista,st,dr)
    quickSort(lista,st,poz-1)
    quickSort(lista,poz+1,dr)

def main():
    l=[int(x) for x in input().split()]
    quickSort(l,0,len(l)-1)
    print(l)

main()