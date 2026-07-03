
def selectionSort(lista):
    for i in range(0,len(lista)-1):
        for j in range(i+1,len(lista)):
            if lista[i]>lista[j]:
                lista[i],lista[j]=lista[j],lista[i]

def main():
    l=[int(x) for x in input().split()]
    selectionSort(l)
    print(l)

main()