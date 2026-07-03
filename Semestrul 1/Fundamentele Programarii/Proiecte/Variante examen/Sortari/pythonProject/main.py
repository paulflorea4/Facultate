def cautare_binara(lista,element,st,dr):
    st=0
    dr=len(lista)-1
    while st<=dr:
        mij=(st+dr)//2
        if lista[mij]==element:
            return mij
        elif element<lista[mij]:
            dr=mij-1
        else:
            st=mij+1
    return -1

def cautare_binara_rec(lista,element,st,dr):
    if st>dr:
        return -1
    mij=(st+dr)//2
    if lista[mij]==element:
        return mij
    elif element<lista[mij]:
        return cautare_binara_rec(lista,element,st,mij-1)
    else:
        return cautare_binara_rec(lista,element,mij+1,dr)

def mergeSort(l,start,end):
    if start>=end-1:
        return
    m=(start+end)//2
    mergeSort(l,start,m)
    mergeSort(l,m,end)
    merge(l,start,end,m)

def merge(l,start,end,m):
    left=l[start:m]
    right=l[m:end]
    i=0
    j=0
    k=start
    while i < len(left) and j < len(right):
        if left[i]<right[j]:
            l[k]=left[i]
            i+=1
        else:
            l[k]=right[j]
            j+=1
        k+=1

    while i < len(left):
        l[k]=left[i]
        i+=1
        k+=1

    while j < len(right):
        l[k]=right[j]
        j+=1
        k+=1

def insertion_sort(lista):
    for i in range(1, len(lista)):
        index = i-1
        copie = lista[i]
        while index >= 0 and copie < lista[index]:
            lista[index + 1] = lista[index]
            index -= 1
        lista[index + 1] = copie

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

def main():
    l=[int(x) for x in input().split()]
    mergeSort(l,0,len(l))
    print(l)

main()