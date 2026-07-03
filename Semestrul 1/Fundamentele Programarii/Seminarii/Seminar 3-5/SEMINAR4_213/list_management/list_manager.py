from domain.melodie import get_titlu, get_artist, creare_melodie, test_creare_melodie, get_gen, get_durata


def add_to_list(lst_melodii: list, melodie: dict) -> None:
    """
    Adauga melodia data la lista de melodii
    :param lst_melodii: lista de melodii
    :param melodie: melodia care se adauga
    :return: -; lista data se modifica prin adaugarea melodiei la finalul listei
    """
    lst_melodii.append(melodie)


def cauta_melodie(lista_melodii: list, titlu_cautat: str, artist_cautat: str) -> dict:
    """
    Cauta o melodie in lista dupa titlu si artist
    :param lista_melodii: lista in care se cauta melodia
    :param titlu_cautat: titlul dupa care se cauta
    :param artist_cautat: artistul dupa care se cauta
    :return: melodia cu titlul si artistul dat, daca aceasta exista in lista
             None, altfel
    """
    for melodie in lista_melodii:
        if get_titlu(melodie) == titlu_cautat and get_artist(melodie) == artist_cautat:
            return melodie


def sterge_melodie(lista_melodii: list, titlu: str, artist: str):
    """
    Sterge melodia cu titlul si artistul dat, daca aceasta exista in lista
    :param lista_melodii: lista de melodii din care se sterge
    :param titlu: titlul melodiei pentru care se incearca stergerea
    :param artist: artistul melodiei pentru care se incearca stergerea
    :return: -; lista_melodii se modifica prin stergerea melodiei cu titlu, artist dat,
                daca aceasta melodie exista; altfel, lista ramane nemodificata
    """
    melodie_cautata = cauta_melodie(lista_melodii, titlu, artist)
    if melodie_cautata is not None:
        lista_melodii.remove(melodie_cautata)


def elimina_dupa_gen(lista_melodii: list, gen: str) -> bool:
    """"
    Elimina din lista data melodiile cu genul specificat
    :param lista_melodii: lista din care se elimina melodiile
    :param gen: genul care se elimina
    :return: True daca lista s-a modificat, False altfel;
            lista primita ca parametru se modifica prin eliminarea
            melodiilor cu genul dat
    """
    # parcurgem lista, daca genul melodiei curente e gen, atunci dam delete

    i = 0
    ok = False
    # lungime_initiala = len(lista_melodii)
    while i < len(lista_melodii):
        if get_gen(lista_melodii[i]) == gen:
            lista_melodii.pop(i)
            ok = True
        else:
            i += 1
    # sau, in loc de folosire ok:
    # return lungime_initiala!=len(lista_melodii)
    return ok


def filtreaza_dupa_durata(lista_melodii: list, durata_inceput: float, durata_final: float) -> list:
    """
    Returneaza o lista de melodii care au durata in intervalul dat
    :param lista_melodii: lista care se filtreaza
    :param durata_inceput: limita inferioara a duratei
    :param durata_final: limita superioara a duratei
    :return: lista de melodii care au durata intre durata_inceput si durata_final
    """

    lista_noua = []
    for elem in lista_melodii:
        if durata_inceput < get_durata(elem) < durata_final:
            lista_noua.append(elem)

    return lista_noua


def add_default_songs(lista_melodii):
    add_to_list(lista_melodii, creare_melodie("Titlu1", "Artist1", "rock", 3.41))
    add_to_list(lista_melodii, creare_melodie("Titlu2", "Artist2", "folk", 5.01))
    add_to_list(lista_melodii, creare_melodie("Titlu3", "Artist3", "folk", 2.33))
    add_to_list(lista_melodii, creare_melodie("Titlu4", "Artist4", "pop", 1.56))
    add_to_list(lista_melodii, creare_melodie("Titlu5", "Artist5", "rock", 13.02))


##########################
# TESTE

def test_add_to_list():
    test_list = []
    assert (len(test_list) == 0)
    add_to_list(test_list, creare_melodie('T1', 'A1', 'folk', 3.40))
    assert (len(test_list) == 1)

    add_to_list(test_list, creare_melodie('T2', 'A2', 'rock', 4.02))
    assert (len(test_list) == 2)


def test_cauta_in_lista():
    test_list = []
    melodie1 = creare_melodie("T1", "A1", "pop", 2.37)
    melodie2 = creare_melodie("T2", "A2", "rock", 2.21)
    melodie3 = creare_melodie("Thunderstruck", "AC/DC", "rock", 3.45)
    add_to_list(test_list, melodie1)
    add_to_list(test_list, melodie2)
    add_to_list(test_list, melodie3)

    assert (cauta_melodie(test_list, "T1", "A1") == melodie1)
    assert (cauta_melodie(test_list, "Thunderstruck", "AC/DC") == melodie3)

    assert (cauta_melodie(test_list, "Highway to Hell", "AC/DC") is None)
    assert (cauta_melodie(test_list, "t1", "a1") is None)
    # inversat artist cu titlu
    assert (cauta_melodie(test_list, "A1", "T1") is None)
    assert (cauta_melodie(test_list, "", "") is None)


def test_stergere_din_lista():
    test_list = []

    melodie1 = creare_melodie("T1", "A1", "pop", 2.37)
    melodie2 = creare_melodie("T2", "A2", "rock", 2.21)
    melodie3 = creare_melodie("Thunderstruck", "AC/DC", "rock", 3.45)

    add_to_list(test_list, melodie1)
    add_to_list(test_list, melodie2)
    add_to_list(test_list, melodie3)

    assert (len(test_list) == 3)
    sterge_melodie(test_list, "T1", "A1")
    assert (len(test_list) == 2)
    sterge_melodie(test_list, "Titlu", "Artist")
    assert (len(test_list) == 2)
    sterge_melodie(test_list, "T2", "A2")
    assert (len(test_list) == 1)
    sterge_melodie(test_list, "Thunderstruck", "AC/DC")
    assert (len(test_list) == 0)

    test_list2 = []
    assert (len(test_list2) == 0)
    sterge_melodie(test_list2, "T1", "A1")
    assert (len(test_list2) == 0)


def test_eliminare_dupa_gen():
    lista_melodii = []
    gen = "abc"
    #functia ar trebui sa returneze false
    assert(not elimina_dupa_gen(lista_melodii, gen))
    assert (len(lista_melodii) == 0)

    melodie1 = creare_melodie("T1", "A1", "pop", 2.37)
    melodie2 = creare_melodie("T2", "A2", "rock", 2.21)
    melodie3 = creare_melodie("Thunderstruck", "AC/DC", "rock", 3.45)
    add_to_list(lista_melodii, melodie1)
    add_to_list(lista_melodii, melodie2)
    add_to_list(lista_melodii, melodie3)

    gen = "rock"
    #functia ar trebui sa returneze true, se elimina melodii
    assert (elimina_dupa_gen(lista_melodii, gen))
    assert (len(lista_melodii) == 1)
    assert (not elimina_dupa_gen(lista_melodii, gen))

    gen = "jghrdhg"
    assert (not elimina_dupa_gen(lista_melodii, gen))
    assert (len(lista_melodii) == 1)

    gen = "pop"
    assert (elimina_dupa_gen(lista_melodii, gen))
    assert (len(lista_melodii) == 0)

    lista_melodii2 = []
    add_to_list(lista_melodii2, creare_melodie("Nunta", "Phoenix", "rock", 2.37))
    add_to_list(lista_melodii2, creare_melodie("Stairway to Heaven", "Led Zeppelin", "rock", 2.21))
    add_to_list(lista_melodii2, creare_melodie("Highway to Hell", "AC/DC", "rock", 3.45))

    gen = "rock"
    assert (elimina_dupa_gen(lista_melodii2, gen))
    assert (len(lista_melodii2) == 0)


def test_filtrare_durata():
    lista_melodii = []

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 1, 2)
    assert (len(lista_filtrata) == 0)

    melodie1 = creare_melodie("Titlu1", "Artist1", "pop", 2.37)
    melodie2 = creare_melodie("Titlu2", "Artist2", "rock", 2.21)
    melodie3 = creare_melodie("Alt Titlu", "Artisti", "rock", 3.45)
    add_to_list(lista_melodii, melodie1)
    add_to_list(lista_melodii, melodie2)
    add_to_list(lista_melodii, melodie3)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 1, 2)
    assert (len(lista_filtrata) == 0)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 2, 2.5)
    assert (len(lista_filtrata) == 2)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 2, 3)
    assert (len(lista_filtrata) == 2)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 2.21, 2.38)
    assert (len(lista_filtrata) == 1)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 5, 9)
    assert (len(lista_filtrata) == 0)

    lista_filtrata = filtreaza_dupa_durata(lista_melodii, 3, 4)
    assert (len(lista_filtrata) == 1)


def ruleaza_teste():
    test_creare_melodie()
    test_add_to_list()
    test_cauta_in_lista()
    test_stergere_din_lista()
    test_eliminare_dupa_gen()
    test_filtrare_durata()
    print("[INFO]: Au trecut toate testele")


