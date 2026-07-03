"""
Functii utile pentru gestionarea listelor
Se pot aplica pentru lista de melodii, lista de undo - fct add_to_list,
copierea unei liste de dictionare (indiferent ce reprezinta dictionarele) - fct copy_list_of_dicts
"""


def add_to_list(lst: list, elem) -> None:
    """
    Adauga elementul elem la lista lst
    :param lst: lista in care se adauga
    :param elem: obiect care se adauga in lista
    :return: -; lista data se modifica prin adaugarea obiectului elem
                la finalul listei
    """
    lst.append(elem)


def copy_list_of_dicts(lst) -> list:
    """
    Creeaza o copie a unei liste de dictionare
    :param lst: lista de dictionare
    :return: o noua lista care contine noi elemente (dictionare) cu aceleasi valori
            ca si cele din lista initiala
            postconditii: id(lst) != id(lista returnata), oricare i, i = 0..len(lst)-1
                          id(lst[i]) != id(lista_returnata[i])
    """
    new_list = []
    for d in lst:
        new_dict = {}
        for key, value in d.items():
            new_dict[key] = value
        new_list.append(new_dict)
    return new_list
