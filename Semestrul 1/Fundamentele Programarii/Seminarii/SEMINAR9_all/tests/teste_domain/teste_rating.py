from domain.rating import Evaluare
from domain.persoana import Persoana
from domain.melodie import Melodie


def test_create_rating():
    melodie = Melodie(1, "Highway Star", "Deep Purple", "rock",4.32)
    persoana = Persoana('2980107123456', 'Alessia')

    rating = Evaluare(melodie, persoana, 4.3)

    assert (rating.melodie == melodie)
    assert (rating.persoana == persoana)
    assert (rating.scor == 4.3)


def test_equal_rating():
    melodie1 = Melodie(1, "Highway Star", "Deep Purple", "rock",4.32)
    persoana1 = Persoana('2970103123456', 'Marcel')

    rating1 = Evaluare(melodie1, persoana1, 10)
    rating2 = Evaluare(melodie1, persoana1, 8.55)
    assert (rating1 == rating2)

    melodie2 = Melodie(2, "Perfect Strangers", "Deep Purple", "rock",3.32)
    rating3 = Evaluare(melodie2, persoana1, 3)
    assert (rating3 != rating2)
