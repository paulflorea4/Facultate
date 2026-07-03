import math
from collections import deque

def ultimul_cuvant_lexicografic(text):
    return max(text.split())

def ultim_cuvant_lexicografic_ai(text):
    maxim = ""
    curent = ""

    for ch in text:
        if ch != " ":
            curent += ch
        else:
            if curent > maxim:
                maxim = curent
            curent = ""

    if curent > maxim:
        maxim = curent

    return maxim

def distanta_euclidiana(a,b):
    return math.sqrt((a[1]-b[1])**2 + (a[0]-b[0])**2)

def distanta_euclidiana_ai(p1, p2):
    dx = p2[0] - p1[0]
    dy = p2[1] - p1[1]
    return (dx * dx + dy * dy) ** 0.5

def produs_scalar(v1,v2):
    p=0
    for i in range(min(len(v1),len(v2))):
        p+=v1[i]*v2[i]
    return p

def vector_la_rar(vec):
    return {i: val for i, val in enumerate(vec) if val != 0}

def produs_scalar_ai(A_list, B_list):
    A = vector_la_rar(A_list)
    B = vector_la_rar(B_list)

    if len(A) > len(B):
        A, B = B, A

    rezultat = 0
    for i, val in A.items():
        if i in B:
            rezultat += val * B[i]
    return rezultat


def cuvinte_unice(text):
    cuvinte = text.split()
    aparitii_cuvinte = {}

    for cuvant in cuvinte:
        aparitii_cuvinte[cuvant] = aparitii_cuvinte.get(cuvant, 0) + 1

    return [cuvant for cuvant in cuvinte if aparitii_cuvinte[cuvant] == 1]


def cuvinte_unice_ai(text):
    cuvinte = text.split()

    frecventa = {}
    for cuvant in cuvinte:
        frecventa[cuvant] = frecventa.get(cuvant, 0) + 1

    unice = [cuvant for cuvant, count in frecventa.items() if count == 1]
    return unice

def valoare_repetata(lista):
    frecventa = {}
    for numar in lista:
        frecventa[numar] = frecventa.get(numar, 0) + 1
        if(frecventa[numar] > 1):
            return numar
    return -1

def valoare_repetata_ai(lista):
    n=len(lista)
    suma_ideala=n*(n-1)//2
    suma_reala=sum(lista)
    return suma_reala-suma_ideala

def element_majoritar(lista):
    frecventa = {}
    for numar in lista:
        frecventa[numar] = frecventa.get(numar, 0) + 1
        if frecventa[numar] > len(lista) // 2:
            return numar
    return -1

def element_majoritar_ai(arr):
    candidat = None
    contor = 0
    for x in arr:
        if contor == 0:
            candidat = x
            contor = 1
        elif x == candidat:
            contor += 1
        else:
            contor -= 1

    if arr.count(candidat) > len(arr) // 2:
        return candidat
    else:
        return None

def element_k(lista,k):
    lista.sort(reverse=True)
    return lista[k-1]


import random


def quickselect(arr, k):

    def partition(left, right):
        pivot_index = random.randint(left, right)
        pivot_value = arr[pivot_index]
        arr[pivot_index], arr[right] = arr[right], arr[pivot_index]
        store_index = left
        for i in range(left, right):
            if arr[i] > pivot_value:
                arr[store_index], arr[i] = arr[i], arr[store_index]
                store_index += 1
        arr[store_index], arr[right] = arr[right], arr[store_index]
        return store_index

    left, right = 0, len(arr) - 1
    k_index = k - 1

    while True:
        pivot_index = partition(left, right)
        if pivot_index == k_index:
            return arr[pivot_index]
        elif pivot_index < k_index:
            left = pivot_index + 1
        else:
            right = pivot_index - 1

def generare_nr_binare(n):
    rezultat = []
    for i in range(1, n + 1):
        rezultat.append(bin(i)[2:])
    return rezultat

def generare_nr_binare_ai(n):
    q = deque()
    q.append('1')
    count = 0
    while count < n:
        curent = q.popleft()
        yield curent
        q.append(curent + '0')
        q.append(curent + '1')
        count += 1


def sume_submatrici(A, lista_perechi):
    rezultate = []

    for (p, q), (r, s) in lista_perechi:
        suma = 0
        for i in range(p, r + 1):
            for j in range(q, s + 1):
                suma += A[i][j]

        rezultate.append(suma)

    return rezultate


def construieste_prefix(A):
    n = len(A)
    m = len(A[0])

    S = [[0] * m for _ in range(n)]

    for i in range(n):
        for j in range(m):
            S[i][j] = A[i][j]

            if i > 0:
                S[i][j] += S[i - 1][j]
            if j > 0:
                S[i][j] += S[i][j - 1]
            if i > 0 and j > 0:
                S[i][j] -= S[i - 1][j - 1]

    return S


def sume_submatrici_ai(A, lista_perechi):
    S = construieste_prefix(A)
    rezultate = []

    for (p, q), (r, s) in lista_perechi:
        suma = S[r][s]

        if p > 0:
            suma -= S[p - 1][s]
        if q > 0:
            suma -= S[r][q - 1]
        if p > 0 and q > 0:
            suma += S[p - 1][q - 1]

        rezultate.append(suma)

    return rezultate


def pozitie_1(linie):
    st = 0
    dr = len(linie) - 1
    poz = len(linie)

    while st <= dr:
        mid = (st + dr) // 2

        if linie[mid] == 1:
            poz = mid
            dr = mid - 1
        else:
            st = mid + 1

    return poz


def linie_max_1(A):
    n = len(A)
    m = len(A[0])

    max_linie = -1
    index_max = -1

    for i in range(n):
        poz = pozitie_1(A[i])
        if m - poz > max_linie:
            max_linie = m - poz
            index_max = i

    return index_max


def prima_pozitie_1(linie):
    st, dr = 0, len(linie) - 1
    poz = len(linie)

    while st <= dr:
        mid = (st + dr) // 2

        if linie[mid] == 1:
            poz = mid
            dr = mid - 1
        else:
            st = mid + 1

    return poz


def linie_max_1_ai(A):
    n = len(A)
    m = len(A[0])

    max_1 = -1
    index_max = -1

    for i in range(n):
        poz = prima_pozitie_1(A[i])
        nr = m - poz

        if nr > max_1:
            max_1 = nr
            index_max = i

    return index_max


def main():
    #1
    text = 'Ana are mere rosii si galbene'
    print("Ultimul cuvant lexicografic:",ultimul_cuvant_lexicografic(text))
    print("Ultimul cuvant lexicografic ai:",ultimul_cuvant_lexicografic(text),'\n')

    #2
    P1 = (1, 5)
    P2 = (4, 1)
    print("Distanta Euclideana:", distanta_euclidiana(P1, P2))
    print("Distanta Euclideana:", distanta_euclidiana_ai(P1, P2),'\n')

    #3
    A = [1, 0, 2, 0, 3]
    B = [1, 2, 0, 3, 1]
    print("Produs scalar:", produs_scalar(A, B))
    print("Produs scalar ai:", produs_scalar_ai(A, B),'\n')

    #4
    text = "ana are ana are mere rosii ana"
    rezultat = cuvinte_unice(text)
    print("Cuvinte care apar o singura data:", rezultat)
    rezultat_ai = cuvinte_unice_ai(text)
    print("Cuvinte care apar o singura data ai:", rezultat_ai,'\n')

    #5
    arr = [1, 2, 3, 4, 2]
    print("Valoarea care se repeta:", valoare_repetata(arr))
    print("Valoarea care se repeta ai:", valoare_repetata_ai(arr),'\n')

    #6
    arr = [2, 8, 7, 2, 2, 5, 2, 3, 1, 2, 2]
    print("Elementul majoritar:", element_majoritar(arr))
    print("Elementul majoritar ai:", element_majoritar_ai(arr),'\n')

    #7
    arr = [4,7,4,6,3,9,1]
    print("Al k-lea element ca marime:", element_k(arr,2))
    print("Al k-lea element ca marime ai:", quickselect(arr,2),'\n')

    #8
    n=10
    rezultat = generare_nr_binare(n)
    print("Toate numerele binare pana la n: ", rezultat)
    rezultat_ai = list(generare_nr_binare_ai(n))
    print("Toate numerele binare pana la n ai: ", rezultat_ai,'\n')

    #9
    A = [
        [0, 2, 5, 4, 1],
        [4, 8, 2, 3, 7],
        [6, 3, 4, 6, 2],
        [7, 3, 1, 8, 3],
        [1, 5, 7, 9, 4]
    ]

    lista_perechi = [
        ((1, 1), (3, 3)),
        ((2, 2), (4, 4))
    ]

    print("Sumele submatricilor:", sume_submatrici(A, lista_perechi))
    print("Sumele submatricilor ai:", sume_submatrici_ai(A, lista_perechi),'\n')

    #10
    A = [[0,0,1,1,1],
        [0,1,1,1,1],
        [0,0,1,1,1]]

    print("Linia care contine cele mai multe 1:", linie_max_1(A))
    print("Linia care contine cele mai multe 1 ai:", linie_max_1_ai(A),'\n')



if __name__=="__main__":
    main()