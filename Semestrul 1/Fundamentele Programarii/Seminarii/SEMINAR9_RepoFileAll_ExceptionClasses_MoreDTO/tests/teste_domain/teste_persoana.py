from domain.persoana import Persoana
from domain.validation import ValidatorPersoana
from exceptions.exceptions import ValidationException


def test_persoana():
    p = Persoana("1234567890123", "Andrei Popescu")
    assert (p.cnp == "1234567890123")
    assert (p.nume == "Andrei Popescu")

    p.nume = "Valentin Mihaila"
    assert (p.nume == "Valentin Mihaila")


def test_equal_persoana():
    p1 = Persoana("1234567890123", "Andrei Popescu")
    p2 = Persoana("1234567890123", "Andrei Pop")
    assert (p1 == p2)

    p3 = Persoana("2134567890123", "Maria Pop")
    assert (p1 != p3)


def test_validare_persoana():
    validator = ValidatorPersoana()
    p1 = Persoana("123", "Andrei Popescu")
    try:
        validator.validate(p1)
        assert False
    except ValidationException:
        assert True

    p2 = Persoana("2234567890123", "Daniel")
    try:
        validator.validate(p2)
        assert False
    except ValidationException:
        assert True

    p3 = Persoana("123", "Andrei")
    try:
        validator.validate(p3)
        assert False
    except ValidationException:
        assert True
