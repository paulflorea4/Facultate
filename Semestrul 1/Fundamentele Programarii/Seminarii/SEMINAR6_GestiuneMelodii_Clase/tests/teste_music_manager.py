from domain.validation import ValidatorMelodie
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
    validator = ValidatorMelodie()
    test_music_manager = MusicManager(validator)
    test_music_manager.add_default_songs()
    return test_music_manager


def test_adauga_melodie():
    validator = ValidatorMelodie()
    music_manager = MusicManager(validator)
    # melodie valida
    music_manager.adauga_melodie("Titlu1", "Artist1", "pop", 2.45)
    assert (len(music_manager.get_melodii()) == 1)

    try:
        # melodie invalida (gen care nu este din lista predefinita)
        music_manager.adauga_melodie("Titlu1", "Artist1", "alt gen", 8.01)
        assert False
    except ValueError:
        assert True

    assert (len(music_manager.get_melodii()) == 1)

    try:
        # melodie invalida (titlu, artist gresit)
        music_manager.adauga_melodie("T", "", "rock", 4.31)
        assert False
    except ValueError:
        assert True
    assert (len(music_manager.get_melodii()) == 1)

    try:
        # melodie invalida (durata gresita)
        music_manager.adauga_melodie("Titlu", "Artist", "rock", 28.82)
        assert False
    except ValueError:
        assert True
    assert (len(music_manager.get_melodii()) == 1)

    # melodie valida
    music_manager.adauga_melodie("Titlu2", "Artist2", "pop", 4.5)
    assert (len(music_manager.get_melodii())== 2)


def test_cauta_in_lista():
    # empty list
    validator = ValidatorMelodie()
    test_music_manager2 = MusicManager(validator)
    m0 = test_music_manager2.cauta_melodie("Titlu1", "Artist1")
    assert (m0 is None)

    test_music_manager = setup_tests()

    m1 = test_music_manager.cauta_melodie("Titlu1", "Artist1")
    assert (m1.get_gen() == "rock")
    assert (m1.get_durata() == 3.41)

    m2 = test_music_manager.cauta_melodie("Titlu5", "Artist5")
    assert (m2.get_gen() == "rock")
    assert (m2.get_durata() == 13.02)

    m3 = test_music_manager.cauta_melodie("Titlu15", "Artist5")
    assert (m3 is None)

    m4 = test_music_manager.cauta_melodie("AC/DC", "Highway to Hell")
    assert (m4 is None)


def test_stergere_din_lista():
    validator = ValidatorMelodie()
    test_music_manager = MusicManager(validator)
    assert (len(test_music_manager.get_melodii()) == 0)
    test_music_manager.sterge_melodie("Titlu1", "Artist1")
    assert (len(test_music_manager.get_melodii()) == 0)

    test_music_manager = setup_tests()

    test_music_manager.sterge_melodie("Titlu1", "Artist1")
    assert (len(test_music_manager.get_melodii()) == 4)

    test_music_manager.sterge_melodie("Titlu1", "Artist1")
    assert (len(test_music_manager.get_melodii()) == 4)

    test_music_manager.sterge_melodie("Titlu5", "Artist5")
    assert (len(test_music_manager.get_melodii()) == 3)

    test_music_manager.sterge_melodie("Titlu3", "Artist3")
    assert (len(test_music_manager.get_melodii()) == 2)

    test_music_manager.sterge_melodie("Titlu", "Artist")
    assert (len(test_music_manager.get_melodii()) == 2)

    test_music_manager.sterge_melodie("Titlu2", "Artist2")
    assert (len(test_music_manager.get_melodii()) == 1)

    test_music_manager.sterge_melodie("Titlu4", "Artist4")
    assert (len(test_music_manager.get_melodii()) == 0)




def test_filtrare_durata():
    validator = ValidatorMelodie()
    test_music_manager = MusicManager(validator)
    lista_filtrata = test_music_manager.filtreaza_dupa_durata(1, 5)
    assert (len(lista_filtrata) == 0)

    test_music_manager = setup_tests()

    lista_filtrata1 = test_music_manager.filtreaza_dupa_durata(1, 5)
    assert (len(lista_filtrata1) == 3)

    lista_filtrata2 = test_music_manager.filtreaza_dupa_durata(5, 10)
    assert (len(lista_filtrata2) == 1)

    lista_filtrata3 = test_music_manager.filtreaza_dupa_durata(4, 5.01)
    assert (len(lista_filtrata3) == 0)

    lista_filtrata4 = test_music_manager.filtreaza_dupa_durata(10, 11)
    assert (len(lista_filtrata4) == 0)

    lista_filtrata5 = test_music_manager.filtreaza_dupa_durata(1, 15)
    assert (len(lista_filtrata5) == 5)


def ruleaza_teste():
    test_adauga_melodie()
    test_cauta_in_lista()
    test_stergere_din_lista()
    test_filtrare_durata()
    print("[INFO]: Au trecut toate testele")


ruleaza_teste()
