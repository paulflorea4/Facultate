def main():
    lista = [int(x) for x in input().split()]
    n = len(lista)
    rez = [0] * n
    rez[-1] = 1
    maxim = 0
    for i in range(n-2, -1, -1):
        pmax = i
        for j in range(i + 1, n):
            if lista[j] >= lista[i] and rez[pmax] < rez[j]:
                pmax = j
        rez[i] = 1 + rez[pmax]
        if rez[i] > maxim:
            maxim = rez[i]
    print(maxim)

if __name__ == '__main__':
    main()
