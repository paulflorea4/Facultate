from domain.persoana import Persoana
from exceptions.exceptions import PersonAlreadyExistsException, PersonDoesNotExistException
from repository.repo_persoane import PersonFileRepository
from utils.file_utils import copy_file_content, clear_file_content

TEST_FILE_PATH = "tests/teste_repository/test_persons.txt"
DEFAULT_FILE_PATH = "tests/teste_repository/default_persons.txt"


def test_read_from_file():
    clear_file_content(TEST_FILE_PATH)
    copy_file_content(DEFAULT_FILE_PATH, TEST_FILE_PATH)
    test_repo = PersonFileRepository(TEST_FILE_PATH)
    assert (test_repo.size() == 7)


def test_store_repo_file():
    clear_file_content(TEST_FILE_PATH)
    test_repo = PersonFileRepository(TEST_FILE_PATH)

    persoana = Persoana('1730523123456', 'Marcel')
    test_repo.store(persoana)
    assert (test_repo.size() == 1)

    try:
        test_repo.store(persoana)
        assert False
    except PersonAlreadyExistsException:
        assert True

    persoana2 = Persoana('1710523123456', 'Marcel')
    test_repo.store(persoana2)
    assert (test_repo.size() == 2)


def test_find_repo_file():
    clear_file_content(TEST_FILE_PATH)
    test_repo = PersonFileRepository(TEST_FILE_PATH)

    assert (test_repo.size() == 0)

    persoana1 = Persoana('1910918123456', "Mircea")
    persoana2 = Persoana('2920918123456', "Carina")
    persoana3 = Persoana('5930918123456', "Alex")

    test_repo.store(persoana1)
    test_repo.store(persoana2)
    test_repo.store(persoana3)

    assert (test_repo.size() == 3)

    assert (test_repo.find('1910918123456') is not None)
    assert (test_repo.find('2920918123456') is not None)
    assert (test_repo.find('5930918123456') is not None)
    assert (test_repo.find('6931118123456') is None)


def test_delete_repo_file():
    clear_file_content(TEST_FILE_PATH)
    test_repo = PersonFileRepository(TEST_FILE_PATH)
    persoana1 = Persoana('1910918123456', "Valentin Mihaila")
    persoana2 = Persoana('2920918123456', "Radu Dragusin")
    persoana3 = Persoana('5930918123456', "Denis Man")

    test_repo.store(persoana1)
    test_repo.store(persoana2)
    test_repo.store(persoana3)
    assert (test_repo.size() == 3)

    deleted_persoana = test_repo.delete('1910918123456')
    assert (test_repo.size() == 2)
    assert (test_repo.find('1910918123456') is None)
    assert (deleted_persoana.nume == "Valentin Mihaila")

    deleted_persoana = test_repo.delete('5930918123456')
    assert (test_repo.size() == 1)
    assert (not test_repo.find('5930918123456'))
    assert (deleted_persoana.nume == "Denis Man")

    test_repo.store(Persoana('5930918123456', 'Alexis'))
    assert (test_repo.size() == 2)

    deleted_persoana = test_repo.delete('5930918123456')
    assert (test_repo.size() == 1)
    assert (test_repo.find('5930918123456') is None)
    assert (deleted_persoana.nume == "Alexis")

    try:
        test_repo.delete('5930918123451')
        assert False
    except PersonDoesNotExistException:
        assert True
