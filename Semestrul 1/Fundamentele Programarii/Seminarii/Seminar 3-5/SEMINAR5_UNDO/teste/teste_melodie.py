from domain.melodie import *
from domain.validare import valideaza_melodie


def test_creare_melodie():
    melodie1 = creare_melodie('T1', 'A1', 'folk', 3.40)
    assert (get_titlu(melodie1) == "T1")
    assert (get_artist(melodie1) == "A1")
    assert (get_gen(melodie1) == "folk")
    assert (get_durata(melodie1) == 3.40)


def test_validare():
    # titlu are doar un caracter (ar trebui sa aiba cel putin 2)
    melodie1 = creare_melodie('T', 'A1', 'folk', 3.40)
    try:
        valideaza_melodie(melodie1)
        assert False
    except ValueError:
        assert True

    # artist str vid, ar trebui sa aiba cel putin un caracter
    melodie2 = creare_melodie('Titlu', '', 'folk', 3.40)
    try:
        valideaza_melodie(melodie2)
        assert False
    except ValueError:
        assert True

    # genul nu e din lista predefinita de genuri acceptate
    melodie3 = creare_melodie('Titlu', 'Artist', 'abc', 3.40)
    try:
        valideaza_melodie(melodie3)
        assert False
    except ValueError:
        assert True

    # durata: minutele nu sunt intre 1 si 15
    melodie4 = creare_melodie('Titlu', 'Artist', 'pop', 20.40)
    try:
        valideaza_melodie(melodie4)
        assert False
    except ValueError:
        assert True

    # durata: secundele nu sunt intre 0 si 59
    melodie5 = creare_melodie('Titlu', 'Artist', 'pop', 3.90)
    try:
        valideaza_melodie(melodie5)
        assert False
    except ValueError:
        assert True

    # melodie invalida din mai multe puncte de vedere
    melodie4 = creare_melodie('T', 'Artist', 'abc', 20.77)
    try:
        valideaza_melodie(melodie4)
        assert False
    except ValueError:
        assert True


test_creare_melodie()
test_validare()
