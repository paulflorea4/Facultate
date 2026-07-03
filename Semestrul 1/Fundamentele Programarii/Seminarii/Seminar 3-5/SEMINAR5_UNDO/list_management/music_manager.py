from domain.melodie import get_titlu, get_artist, creare_melodie, get_gen, get_durata
from domain.validare import valideaza_melodie
from utils.list_utils import copy_list_of_dicts, add_to_list


def creeaza_music_manager() -> dict:
    """
    Creeaza manager pentru melodii: contine atat lista curenta, cat si lista de undo
    :return: dict cu 2 chei: 'lista_curenta', valoarea asociata este lista curenta de melodii, initial vida
                             'lista_undo', lista de undo care contine starile anterioare ale listei curente, initial vida
    """
    return {'lista_curenta': [],
            'lista_undo': []}


def get_lista_melodii(music_manager: dict) -> list:
    return music_manager['lista_curenta']


def get_lista_undo(music_manager: dict) -> list:
    return music_manager['lista_undo']


def set_lista_curenta(music_manager: dict, lista_curenta_noua) -> None:
    music_manager['lista_curenta'] = lista_curenta_noua


def set_lista_undo(music_manager: dict, lista_undo_noua) -> None:
    music_manager['lista_undo'] = lista_undo_noua


def adauga_la_lista_undo(music_manager: dict, lista: list) -> None:
    add_to_list(music_manager['lista_undo'], lista)


def adauga_melodie(music_manager: dict, titlu: str, artist: str, gen: str, durata: float) -> None:
    """
    Adauga o melodie la lista de melodii
    :param music_manager: manager-ul de muzica gestionata de aplicatie
    :param titlu: titlul melodiei pe care vrem sa o adaugam
    :param artist: artistul melodiei pe care vrem sa o adaugam
    :param gen: genul melodiei pe care vrem sa o adaugam
    :param durata: durata melodiei pe care vrem sa o adaugam
    :return: -; lista data se modifica prin adaugarea melodiei cu informatiile date
    :raises: ValueError daca melodia nu este valida
    """
    melodie = creare_melodie(titlu, artist, gen, durata)
    valideaza_melodie(melodie)
    adauga_la_lista_undo(music_manager, copy_list_of_dicts(get_lista_melodii(music_manager)))
    add_to_list(get_lista_melodii(music_manager), melodie)


def cauta_melodie(music_manager: dict, titlu_cautat: str, artist_cautat: str) -> dict:
    """
    Cauta o melodie in lista dupa titlu si artist
    :param music_manager: manager-ul de muzica gestionata de aplicatie
    :param titlu_cautat: titlul dupa care se cauta
    :param artist_cautat: artistul dupa care se cauta
    :return: melodia cu titlul si artistul dat, daca aceasta exista in lista
             None, altfel
    """
    lista_melodii = get_lista_melodii(music_manager)
    for melodie in lista_melodii:
        if get_titlu(melodie) == titlu_cautat and get_artist(melodie) == artist_cautat:
            return melodie


def sterge_melodie(music_manager: dict, titlu: str, artist: str):
    """
    Sterge melodia cu titlul si artistul dat, daca aceasta exista in lista
    :param music_manager: manager-ul de muzica gestionata de aplicatie
    :param titlu: titlul melodiei pentru care se incearca stergerea
    :param artist: artistul melodiei pentru care se incearca stergerea
    :return: -; lista_melodii se modifica prin stergerea melodiei cu titlu, artist dat,
                daca aceasta melodie exista; altfel, lista ramane nemodificata
    """
    melodie_cautata = cauta_melodie(music_manager, titlu, artist)
    if melodie_cautata is not None:
        lista_melodii = get_lista_melodii(music_manager)
        lista_melodii.remove(melodie_cautata)


def elimina_dupa_gen(music_manager: dict, gen: str):
    """"
    Elimina din lista data melodiile cu genul specificat
    :param music_manager: manager-ul care contine atat lista de melodii, cat si lista de undo
    :param gen: genul care se elimina
    :return: lista de melodii se modifica prin eliminarea melodiilor cu genul dat
    """
    # parcurgem lista, daca genul melodiei curente e gen, atunci dam delete

    i = 0
    lista_melodii = get_lista_melodii(music_manager)
    while i < len(lista_melodii):
        if get_gen(lista_melodii[i]) == gen:
            lista_melodii.pop(i)
        else:
            i += 1


def filtreaza_dupa_durata(music_manager: dict, durata_inceput: float, durata_final: float) -> list:
    """
    Returneaza o lista de melodii care au durata in intervalul dat
    :param music_manager: manager-ul care contine atat lista de melodii, cat si lista de undo
    :param durata_inceput: limita inferioara a duratei
    :param durata_final: limita superioara a duratei
    :return: lista de melodii care au durata intre durata_inceput si durata_final
    """

    lista_noua = []
    for elem in get_lista_melodii(music_manager):
        if durata_inceput < get_durata(elem) < durata_final:
            lista_noua.append(elem)

    return lista_noua


def add_default_songs(music_manager):
    lista_melodii = get_lista_melodii(music_manager)
    add_to_list(lista_melodii, creare_melodie("Titlu1", "Artist1", "rock", 3.41))
    add_to_list(lista_melodii, creare_melodie("Titlu2", "Artist2", "folk", 5.01))
    add_to_list(lista_melodii, creare_melodie("Titlu3", "Artist3", "folk", 2.33))
    add_to_list(lista_melodii, creare_melodie("Titlu4", "Artist4", "pop", 1.56))
    add_to_list(lista_melodii, creare_melodie("Titlu5", "Artist5", "rock", 13.02))


def undo(music_manager):
    """
    Anuleaza ultima operatie efectuata (operatie care modifica lista)
    :param music_manager: manager-ul de muzica gestionata de aplicatie
    :return: -; lista curenta este setata la starea listei de dinaintea ultimei operatii
                care a modificat lista
    """
    if len(get_lista_undo(music_manager)) == 0:
        raise ValueError("Nu se mai poate face undo.")

    #se face un "revert" la ultima stare a listei de dinaintea
    #efectuarii operatiei la care se face undo
    lista_anterioara = get_lista_undo(music_manager).pop()
    set_lista_curenta(music_manager, lista_anterioara)
