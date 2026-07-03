from list_management.music_manager import *


def setup_tests():
    """
    Creeaza un music_manager de pornire, pentru a nu duplica acest cod in fiecare test
    :return: music_manager cu o serie de melodii adaugate by default,

    "Titlu1", "Artist1", "rock", 3.41
    "Titlu2", "Artist2", "folk", 5.01
    "Titlu3", "Artist3", "folk", 2.33
    "Titlu4", "Artist4", "pop", 1.56
    "Titlu5", "Artist5", "rock", 13.02
    """
    test_music_manager = creeaza_music_manager()
    add_default_songs(test_music_manager)
    return test_music_manager


def test_adauga_melodie():
    music_manager = creeaza_music_manager()
    # melodie valida
    adauga_melodie(music_manager, "Titlu1", "Artist1", "pop", 2.45)
    assert (len(get_lista_melodii(music_manager)) == 1)

    try:
        # melodie invalida (gen care nu este din lista predefinita)
        adauga_melodie(music_manager, "Titlu1", "Artist1", "alt gen", 8.01)
        assert False
    except ValueError:
        assert True

    assert (len(get_lista_melodii(music_manager)) == 1)

    try:
        # melodie invalida (titlu, artist gresit)
        adauga_melodie(music_manager, "T", "", "rock", 4.31)
        assert False
    except ValueError:
        assert True
    assert (len(get_lista_melodii(music_manager)) == 1)

    try:
        # melodie invalida (durata gresita)
        adauga_melodie(music_manager, "Titlu", "Artist", "rock", 28.82)
        assert False
    except ValueError:
        assert True
    assert (len(get_lista_melodii(music_manager)) == 1)

    # melodie valida
    adauga_melodie(music_manager, "Titlu2", "Artist2", "pop", 4.5)
    assert (len(get_lista_melodii(music_manager)) == 2)


def test_cauta_in_lista():
    # empty list
    test_music_manager2 = creeaza_music_manager()
    m0 = cauta_melodie(test_music_manager2, "Titlu1", "Artist1")
    assert (m0 is None)

    test_music_manager = setup_tests()

    m1 = cauta_melodie(test_music_manager, "Titlu1", "Artist1")
    assert (get_gen(m1) == "rock")
    assert (get_durata(m1) == 3.41)

    m2 = cauta_melodie(test_music_manager, "Titlu5", "Artist5")
    assert (get_gen(m2) == "rock")
    assert (get_durata(m2) == 13.02)

    m3 = cauta_melodie(test_music_manager, "Titlu15", "Artist5")
    assert (m3 is None)

    m4 = cauta_melodie(test_music_manager, "AC/DC", "Highway to Hell")
    assert (m4 is None)


def test_stergere_din_lista():
    test_music_manager = creeaza_music_manager()
    assert (len(get_lista_melodii(test_music_manager)) == 0)
    sterge_melodie(test_music_manager, "Titlu1", "Artist1")
    assert (len(get_lista_melodii(test_music_manager)) == 0)

    test_music_manager = setup_tests()

    sterge_melodie(test_music_manager, "Titlu1", "Artist1")
    assert (len(get_lista_melodii(test_music_manager)) == 4)

    sterge_melodie(test_music_manager, "Titlu1", "Artist1")
    assert (len(get_lista_melodii(test_music_manager)) == 4)

    sterge_melodie(test_music_manager, "Titlu5", "Artist5")
    assert (len(get_lista_melodii(test_music_manager)) == 3)

    sterge_melodie(test_music_manager, "Titlu3", "Artist3")
    assert (len(get_lista_melodii(test_music_manager)) == 2)

    sterge_melodie(test_music_manager, "Titlu", "Artist")
    assert (len(get_lista_melodii(test_music_manager)) == 2)

    sterge_melodie(test_music_manager, "Titlu2", "Artist2")
    assert (len(get_lista_melodii(test_music_manager)) == 1)

    sterge_melodie(test_music_manager, "Titlu4", "Artist4")
    assert (len(get_lista_melodii(test_music_manager)) == 0)


def test_eliminare_dupa_gen():
    test_music_manager1 = creeaza_music_manager()
    assert (len(get_lista_melodii(test_music_manager1)) == 0)
    elimina_dupa_gen(test_music_manager1, 'pop')
    assert (len(get_lista_melodii(test_music_manager1)) == 0)
    elimina_dupa_gen(test_music_manager1, 'folk')
    assert (len(get_lista_melodii(test_music_manager1)) == 0)

    test_music_manager2 = setup_tests()

    elimina_dupa_gen(test_music_manager2, 'pop')
    assert (len(get_lista_melodii(test_music_manager2)) == 4)

    elimina_dupa_gen(test_music_manager2, 'pop')
    assert (len(get_lista_melodii(test_music_manager2)) == 4)

    elimina_dupa_gen(test_music_manager2, 'rock')
    assert (len(get_lista_melodii(test_music_manager2)) == 2)
    elimina_dupa_gen(test_music_manager2, 'folk')
    assert (len(get_lista_melodii(test_music_manager2)) == 0)


def test_filtrare_durata():
    test_music_manager = creeaza_music_manager()
    lista_filtrata = filtreaza_dupa_durata(test_music_manager, 1, 5)
    assert (len(lista_filtrata) == 0)

    test_music_manager = setup_tests()

    lista_filtrata1 = filtreaza_dupa_durata(test_music_manager, 1, 5)
    assert (len(lista_filtrata1) == 3)

    lista_filtrata2 = filtreaza_dupa_durata(test_music_manager, 5, 10)
    assert (len(lista_filtrata2) == 1)

    lista_filtrata3 = filtreaza_dupa_durata(test_music_manager, 4, 5.01)
    assert (len(lista_filtrata3) == 0)

    lista_filtrata4 = filtreaza_dupa_durata(test_music_manager, 10, 11)
    assert (len(lista_filtrata4) == 0)

    lista_filtrata5 = filtreaza_dupa_durata(test_music_manager, 1, 15)
    assert (len(lista_filtrata5) == 5)


def ruleaza_teste():
    test_adauga_melodie()
    test_cauta_in_lista()
    test_stergere_din_lista()
    test_eliminare_dupa_gen()
    test_filtrare_durata()
    print("[INFO]: Au trecut toate testele")


ruleaza_teste()
