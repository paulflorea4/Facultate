from domain.melodie import *
from domain.validation import ValidatorMelodie


def test_melodie():
    melodie1 = Melodie(1, 'T1', 'A1', 'folk', 3.40)
    assert (melodie1.get_titlu() == "T1")
    assert (melodie1.get_artist() == "A1")
    assert (melodie1.get_gen() == "folk")
    assert (melodie1.get_durata() == 3.40)

    melodie1.set_gen("rock")
    assert (melodie1.get_gen() == "rock")

    melodie1.set_titlu("ABC")
    assert (melodie1.get_titlu() == "ABC")

    melodie1.set_artist("XYZ")
    assert (melodie1.get_artist() == "XYZ")

    melodie1.set_durata(1.33)
    assert (melodie1.get_durata() == 1.33)


def test_validare_melodie():
    # titlu are doar un caracter (ar trebui sa aiba cel putin 2)
    melodie1 = Melodie(1, 'T', 'A1', 'folk', 3.40)
    validator = ValidatorMelodie()
    try:
        validator.validate(melodie1)
        assert False
    except ValueError:
        assert True

    # artist str vid, ar trebui sa aiba cel putin un caracter
    melodie2 = Melodie(2, 'Titlu', '', 'folk', 3.40)
    try:
        validator.validate(melodie2)
        assert False
    except ValueError:
        assert True

    # genul nu e din lista predefinita de genuri acceptate
    melodie3 = Melodie(3, 'Titlu', 'Artist', 'abc', 3.40)
    try:
        validator.validate(melodie3)
        assert False
    except ValueError:
        assert True

    # durata: minutele nu sunt intre 1 si 15
    melodie4 = Melodie(4, 'Titlu', 'Artist', 'pop', 20.40)
    try:
        validator.validate(melodie4)
        assert False
    except ValueError:
        assert True

    # durata: secundele nu sunt intre 0 si 59
    melodie5 = Melodie(5, 'Titlu', 'Artist', 'pop', 3.90)
    try:
        validator.validate(melodie5)
        assert False
    except ValueError:
        assert True

    # melodie invalida din mai multe puncte de vedere
    melodie4 = Melodie(6, 'T', 'Artist', 'abc', 20.77)
    try:
        validator.validate(melodie4)
        assert False
    except ValueError:
        assert True


def test_equal_melodie():
    melodie1 = Melodie(1, 'T1', 'A1', 'folk', 3.40)
    melodie2 = Melodie(1, 'T1', 'A1', 'folk', 3.40)
    assert (melodie1 == melodie2)

    melodie3 = Melodie(2, 'T1', 'A1', 'folk', 3.40)
    assert (melodie1 != melodie3)

