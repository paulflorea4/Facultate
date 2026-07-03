def mergeSort(objects, start, end, key,reverse):
    """
    Functia de sortare ce utilizeaza metoda mergeSort
    :param objects: lista de obiecte de sortat
    :param start: index ul de inceput al listei
    :param end: index ul de sfarsit al listei
    :param key: functie pentru transformarea elementelor inainte de comparare
    :param reverse: modul de sortare (crescator / descrescator)
    :return:-
    """
    if end - start <= 1:
        return

    mid = (start + end) // 2
    mergeSort(objects, start, mid, key,reverse)
    mergeSort(objects, mid, end, key,reverse)
    merge(objects, start, end, mid, key,reverse)


def merge(objects, start, end, mid, key:None,reverse=False):
    """
    Functie de interclasare a 2 jumatati de lista sortate dupa un key
    :param objects:lista de obiecte
    :param start:index ul de inceput al listei
    :param end:index ul de sfarsit al listei
    :param mid:index ul de mijloc al listei
    :param key:functie pentru transformarea elementelor inainte de comparare
    :param reverse:modul de sortare (crescator / descrescator)
    :return:-
    """
    if key is None:
        key = lambda x: x

    left = objects[start:mid]
    right = objects[mid:end]

    i = 0
    j = 0
    k = start

    while i < len(left) and j < len(right):
        if (key(left[i]) <= key(right[j]) and not reverse) or (key(left[i]) > key(right[j]) and reverse):
            objects[k] = left[i]
            i += 1
        else:
            objects[k] = right[j]
            j += 1
        k += 1

    while i < len(left):
        objects[k] = left[i]
        i += 1
        k += 1

    while j < len(right):
        objects[k] = right[j]
        j += 1
        k += 1

def bingoSort(objects,size, key=None, reverse=False):
    """
    Functia de sortare ce utilizeaza metoda bingoSort
    :param objects: lista de obiecte de sortat
    :param size: dimensiunea listei
    :param key: functie pentru transformarea elementelor inainte de comparare
    :param reverse: modul de sortare (crescator / descrescator)
    :return: -
    """

    if key is None:
        key = lambda x: x

    bingo = min(objects, key=key)
    largest = max(objects, key=key)
    nextBingo = largest
    nextPos = 0

    while key(bingo) < key(nextBingo):
        startPos = nextPos
        for i in range(startPos, size):
            if key(objects[i]) == key(bingo):
                objects[i], objects[nextPos] = objects[nextPos], objects[i]
                nextPos += 1

            elif key(objects[i]) < key(nextBingo):
                nextBingo = objects[i]

        bingo = nextBingo
        nextBingo = largest

    if reverse:
        objects.reverse()

