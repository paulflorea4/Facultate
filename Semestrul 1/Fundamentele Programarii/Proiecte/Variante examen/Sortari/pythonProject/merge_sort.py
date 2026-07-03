
def mergeSort(lista,start,end):
    if start>=end-1:
        return
    m=(start+end)//2
    mergeSort(lista,start,m)
    mergeSort(lista,m,end)

    left=lista[start:m]
    right=lista[m:end]
    i=0
    j=0
    k=start
    while i<len(left) and j<len(right):
        if left[i]<right[j]:
            lista[k]=left[i]
            i+=1
        else:
            lista[k]=right[j]
            j+=1
        k+=1

    while i<len(left):
        lista[k] = left[i]
        i += 1
        k += 1

    while j<len(right):
        lista[k] = right[j]
        j += 1
        k += 1

def main():
    l=[int(x) for x in input().split()]
    mergeSort(l,0,len(l))
    print(l)

main()