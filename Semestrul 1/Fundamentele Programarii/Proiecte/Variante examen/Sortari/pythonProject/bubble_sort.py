def bubbleSort(lista):
    sorted=False
    while not sorted:
        sorted=True
        for i in range(0,len(lista)-1):
            if lista[i+1]<lista[i]:
                lista[i],lista[i+1]=lista[i+1],lista[i]
                sorted=False

def main():
    l=[int(x) for x in input().split()]
    bubbleSort(l)
    print(l)

main()