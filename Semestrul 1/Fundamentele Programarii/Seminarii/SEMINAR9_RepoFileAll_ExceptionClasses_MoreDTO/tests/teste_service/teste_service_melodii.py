from domain.validation import ValidatorMelodie
from exceptions.exceptions import SongAlreadyExistsException, SongDoesNotExistException, ValidationException
from repository.repo_melodii import SongMemoryRepository
from service.song_controller import ServiceMelodii


def test_add_service():
    test_repo = SongMemoryRepository()
    test_val = ValidatorMelodie()
    test_srv = ServiceMelodii(test_repo, test_val)
    assert (len(test_srv.get_all()) == 0)

    test_srv.adauga_melodie(1, "Highway Star", "Deep Purple", "rock", 3.24)
    assert (len(test_srv.get_all()) == 1)

    try:
        test_srv.adauga_melodie(1, "Stormbringer", "Deep Purple", "rock", 6.02)
        assert False
    except SongAlreadyExistsException:
        assert True

    try:
        test_srv.adauga_melodie(163, "", "", "rock", 6.02)
        assert False
    except ValidationException:
        assert True

    try:
        test_srv.adauga_melodie(10, "Another brick in the wall", "Pink Floyd", "rock", 21.89)
        assert False
    except ValidationException:
        assert True


def test_actualizeaza_service():
    test_repo = SongMemoryRepository()
    test_val = ValidatorMelodie()
    test_srv = ServiceMelodii(test_repo, test_val)
    assert (len(test_srv.get_all()) == 0)

    test_srv.adauga_melodie(1, "Ielele", "Cargo", "rock", 4.24)
    test_srv.adauga_melodie(2, "Mockingbird", "Eminem", "hip-hop", 3.24)
    test_srv.adauga_melodie(3, "Ploaia care va veni", "Pasarea Colibri", "folk", 3.24)
    assert (len(test_srv.get_all()) == 3)

    test_srv.actualizeaza_melodie(1, "Ielele", "Cargo", "rock", 6.02)
    new_m = test_srv.find_melodie(1)
    assert (new_m.get_durata() == 6.02)

    try:
        test_srv.actualizeaza_melodie(3, "", "", "rock", 5.2)
        assert False
    except ValidationException:
        assert True

    try:
        test_srv.actualizeaza_melodie(16, "Counting Stars", "OneRepublic", "rock", 5.2)
        assert False
    except SongDoesNotExistException:
        assert True


def test_filter_service():
    test_repo = SongMemoryRepository()
    test_val = ValidatorMelodie()
    test_srv = ServiceMelodii(test_repo, test_val)
    assert (len(test_srv.get_all()) == 0)

    test_srv.adauga_melodie(1, "Ielele", "Cargo", "rock", 4.24)
    test_srv.adauga_melodie(2, "Mockingbird", "Eminem", "hip-hop", 6.18)
    test_srv.adauga_melodie(3, "Ploaia care va veni", "Pasarea Colibri", "folk", 3.20)
    test_srv.adauga_melodie(4, "Lose yourself", "Eminem", "hip-hop", 2.11)
    test_srv.adauga_melodie(5, "Counting Stars", "OneRepublic", "pop", 3.15)

    assert (len(test_srv.get_all()) == 5)

    filtered_list1 = test_srv.filtreaza_dupa_durata(1, 15)
    assert (len(filtered_list1) == len(test_srv.get_all()))

    filtered_list2 = test_srv.filtreaza_dupa_durata(1, 3)
    assert (len(filtered_list2) == 1)

    filtered_list3 = test_srv.filtreaza_dupa_durata(3, 4)
    assert (len(filtered_list3) == 2)

    filtered_list4 = test_srv.filtreaza_dupa_durata(1, 6.18)
    assert (len(filtered_list4) == 4)

    filtered_list5 = test_srv.filtreaza_dupa_durata(8, 15)
    assert (len(filtered_list5) == 0)

    filtered_list6 = test_srv.filtreaza_dupa_durata(3.10, 3.59)
    assert (len(filtered_list6) == 2)
