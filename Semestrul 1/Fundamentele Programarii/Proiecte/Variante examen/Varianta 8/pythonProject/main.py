def cerinta1(lista):
    for i in range(0, len(lista) - 1):
        for j in range(0, len(lista) - 1):
            if lista[j] > lista[j + 1]:
                lista[j], lista[j + 1] = lista[j + 1], lista[j]
    txt = ""
    for el in lista:
        txt += str(el) + " "
    print(txt)

def prod_poz_pare(lista, st, dr):
    if st == dr:
        if st % 2 == 0: return lista[st]
        return 1
    mijloc = (st + dr) // 2
    return (prod_poz_pare(lista, st, mijloc) *
            prod_poz_pare(lista, mijloc + 1, dr))


def cerinta4(lista):
    if lista == []:
        return []
    if len(lista) == 1:
        return [2 * lista[0]]
    mijloc = len(lista) // 2
    return cerinta4(lista[:mijloc]) + cerinta4(lista[mijloc:])

def main():
    #input()
    lista = [int(x) for x in input().split()]
    #cerinta1(lista)
    #print(cerinta4(lista))
    #print(prod_poz_pare(lista, 0, len(lista) - 1))
    sclm(lista)

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
                if (lista[j] > lista[i] and
                        r[j] > r[pmax]):
                    pmax = j
                    p[i] = j
            r[i] = 1 + r[pmax]
    pc = 0
    for i in range(1, n):
        if r[pc] < r[i]:
            pc = i
    txt = ""
    while pc != -1:
        txt += str(lista[pc]) + " "
        pc = p[pc]
    print(txt)

if __name__ == '__main__':
    main()