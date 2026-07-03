from domain.melodie import Melodie
from repository.repository import RepositoryMelodii


def test_store_repo():
    test_repo = RepositoryMelodii()
    assert (test_repo.get_size() == 0)

    m = Melodie(1, "When the levee breaks", "Led Zeppelin", "rock", 6.41)
    test_repo.store(m)
    assert (test_repo.get_size() == 1)

    m2 = Melodie(1, "Kashmir", "Led Zeppelin", "rock", 3.16)

    try:
        test_repo.store(m2)
        assert False
    except ValueError:
        assert True

    assert (test_repo.get_size() == 1)

    test_repo.store(Melodie(4, "Dealul cu Dor", "Pasarea Colibri", "folk", 3.21))
    assert (test_repo.get_size() == 2)


def test_update_repo():
    test_repo = RepositoryMelodii()
    assert (test_repo.get_size() == 0)

    m = Melodie(1, "When the levee breaks", "Led Zeppelin", "rock", 6.41)
    try:
        test_repo.update(m)
        assert False
    except ValueError:
        assert True

    test_repo.store(m)
    assert (test_repo.get_size() == 1)

    m2 = Melodie(1, "Kashmir", "Led Zeppelin", "rock", 3.16)

    test_repo.update(m2)

    assert (test_repo.get_size() == 1)
    m_modif = test_repo.find(1)
    assert (m_modif.get_titlu() == "Kashmir")
    assert (m_modif.get_artist() == "Led Zeppelin")
    assert (m_modif.get_gen() == "rock")
    assert (m_modif.get_durata() == 3.16)

    new_m = Melodie(4, "Dealul cu Dor", "Pasarea Colibri", "folk", 3.21)
    try:
        test_repo.update(new_m)
        assert False
    except ValueError:
        assert True
    assert (test_repo.get_size() == 1)


def test_find_repo():
    test_repo = RepositoryMelodii()
    assert (test_repo.get_size() == 0)
    m_found = test_repo.find(1)
    assert (m_found is None)

    m1 = Melodie(1, "When the levee breaks", "Led Zeppelin", "rock", 6.41)
    m2 = Melodie(2, "Kashmir", "Led Zeppelin", "rock", 3.16)
    m3 = Melodie(3, "Counting Stars", "OneRepublic", "pop", 3.2)

    test_repo.store(m1)
    test_repo.store(m2)
    test_repo.store(m3)

    assert (test_repo.get_size() == 3)

    m1_found = test_repo.find(1)
    assert (m1_found.get_titlu() == "When the levee breaks")
    assert (m1_found.get_durata() == 6.41)

    m2_found = test_repo.find(2)
    assert (m2_found.get_titlu() == "Kashmir")
    assert (m2_found.get_durata() == 3.16)

    m3_found = test_repo.find(3)
    assert (m3_found.get_titlu() == "Counting Stars")
    assert (m3_found.get_artist() == "OneRepublic")
    assert (m3_found.get_gen() == "pop")
    assert (m3_found.get_durata() == 3.2)

    m4_found = test_repo.find(4)
    assert (m4_found is None)
