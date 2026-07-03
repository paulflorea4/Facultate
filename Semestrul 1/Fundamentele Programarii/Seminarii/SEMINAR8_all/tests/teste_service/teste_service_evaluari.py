from domain.melodie import Melodie
from domain.validation import ValidatorRating
from repository.repo_melodii import SongMemoryRepository
from repository.repo_persoane import PersonFileRepository
from repository.repo_ratings import RatingMemoryRepository
from service.rating_controller import ServiceRatings
from utils.file_utils import copy_file_content, clear_file_content

TEST_FILE_PATH = "tests/teste_service/test_persons.txt"
DEFAULT_ENTITIES_PATH = "tests/teste_service/default_persons.txt"


def test_add_assignment():
    song_repo = SongMemoryRepository()
    song_repo.store(Melodie(1, "Highway Star", "Deep Purple", "rock", 4.21))

    clear_file_content(TEST_FILE_PATH)
    copy_file_content(DEFAULT_ENTITIES_PATH, TEST_FILE_PATH)
    person_repo = PersonFileRepository(TEST_FILE_PATH)

    evaluare_repo = RatingMemoryRepository()
    assignment_validator = ValidatorRating()
    assignment_service = ServiceRatings(song_repo, person_repo, evaluare_repo, assignment_validator)
    # all ok, can add
    assignment_service.adauga_rating(1, '6050706437566', 4)
    assert (len(assignment_service.get_all()) == 1)

    assignment_service.adauga_rating(1, '1760920213245', 3.21)
    assert (len(assignment_service.get_all()) == 2)

    # add same assignment
    try:
        assignment_service.adauga_rating(1, '1760920213245', 3.75)
        assert False
    except ValueError:
        assert True

    # person doesn't exist
    try:
        assignment_service.adauga_rating(1, '1760920213241', 5)
        assert False
    except ValueError:
        assert True

    # song doesn't exist
    try:
        assignment_service.adauga_rating(1347, '1760920213245', 2.335)
        assert False
    except ValueError:
        assert True

    # incorrect evaluation score
    try:
        assignment_service.adauga_rating(1, '1760920213245', 100)
        assert False
    except ValueError:
        assert True


def test_most_listened_to():
    song_repo = SongMemoryRepository()
    song_repo.store(Melodie(1, "Highway Star", "Deep Purple", "rock", 4.21))
    song_repo.store(Melodie(2, "Bohemian Rhapsody", "Queen", "rock", 5.55))
    song_repo.store(Melodie(3, "Stairway to Heaven", "Led Zeppelin", "rock", 8.02))
    song_repo.store(Melodie(4, "Shape of You", "Ed Sheeran", "pop", 4.24))
    song_repo.store(Melodie(5, "Blinding Lights", "The Weeknd", "pop", 3.20))
    song_repo.store(Melodie(6, "Smells Like Teen Spirit", "Nirvana", "rock", 5.01))
    song_repo.store(Melodie(7, "Hotel California", "Eagles", "rock", 6.31))
    song_repo.store(Melodie(8, "Billie Jean", "Michael Jackson", "pop", 4.54))
    song_repo.store(Melodie(9, "Imagine", "John Lennon", "rock", 3.03))
    song_repo.store(Melodie(10, "Lose Yourself", "Eminem", "hip-hop", 5.26))
    song_repo.store(Melodie(11, "Rolling in the Deep", "Adele", "pop", 3.48))

    clear_file_content(TEST_FILE_PATH)
    copy_file_content(DEFAULT_ENTITIES_PATH, TEST_FILE_PATH)
    person_repo = PersonFileRepository(TEST_FILE_PATH)

    evaluare_repo = RatingMemoryRepository()
    assignment_validator = ValidatorRating()
    assignment_service = ServiceRatings(song_repo, person_repo, evaluare_repo, assignment_validator)

    assignment_service.adauga_rating(1, '6050706437566', 5)
    assignment_service.adauga_rating(1, '5040703757453', 4.1)
    assignment_service.adauga_rating(1, '1760920213245', 3.2)
    assignment_service.adauga_rating(1, '1991002122222', 4.30)

    assignment_service.adauga_rating(2, '6050706437566', 3.4)
    assignment_service.adauga_rating(2, '5040703757453', 2.32)
    assignment_service.adauga_rating(2, '1991002122222', 3.5)

    assignment_service.adauga_rating(4, '6050706437566', 4.2)
    assignment_service.adauga_rating(4, '5040703757453', 3.4)

    assignment_service.adauga_rating(5, '6050706437566', 1.3)
    assignment_service.adauga_rating(5, '5040703757453', 3.45)
    assignment_service.adauga_rating(5, '1991002122222', 4.21)

    assignment_service.adauga_rating(6, '1991002122222', 3.8)
    assignment_service.adauga_rating(6, '1681210156348', 2.53)

    assignment_service.adauga_rating(8, '5040703757453', 4.23)
    assignment_service.adauga_rating(8, '1760920213245', 4.21)

    assignment_service.adauga_rating(9, '5040703757453', 3.54)

    assignment_service.adauga_rating(10, '6050706437566', 2.9)
    assignment_service.adauga_rating(10, '5040703757453', 4.9)

    # not ideal that we access fields directly
    d = assignment_service.most_listened_to()
    assert (d[1]['nr_ascultari'] == 4)
    assert (d[2]['nr_ascultari'] == 3)
    assert (d[4]['nr_ascultari'] == 2)
    assert (d[5]['nr_ascultari'] == 3)
