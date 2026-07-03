def insertionSort(lista):
    for i in range(1,len(lista)):
        index=i-1
        a=lista[i]
        while index>=0 and a<lista[index]:
            lista[index+1]=lista[index]
            index-=1
        lista[index+1]=a

def main():
    l=[int(x) for x in input().split()]
    insertionSort(l)
    print(l)

main()