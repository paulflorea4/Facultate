import unittest

from domain.validation import ValidatorRating
from exceptions.exceptions import RatingAlreadyExistsException, PersonDoesNotExistException, SongDoesNotExistException, \
    ValidationException
from repository.repo_melodii import SongFileRepository
from repository.repo_persoane import PersonFileRepository
from repository.repo_ratings import RatingFileRepository
from service.rating_controller import ServiceRatings
from utils.file_utils import clear_file_content, copy_file_content

#Example for what is required in Lab 10 (PyUnit)
#Read more: https://docs.python.org/3/library/unittest.html
#discutat la cursul 8, discutam si la Seminarul 10 - puteti sa imi scrieti daca sunt intrebari
class TestsRatingService(unittest.TestCase):
    def setUp(self):
        print("setUp called")
        #method that unittest calls before running each test method in a test case class
        copy_file_content('melodii_default.txt', 'test_melodii.txt')
        song_repo = SongFileRepository('test_melodii.txt')

        copy_file_content('persons_default.txt', 'test_persons.txt')
        person_repo = PersonFileRepository('test_persons.txt')

        copy_file_content('evaluari_default.txt', 'test_evaluari.txt')
        rating_repo = RatingFileRepository("test_evaluari.txt")
        rating_validator = ValidatorRating()
        self.__rating_service = ServiceRatings(song_repo, person_repo, rating_repo, rating_validator)

    def test_add_rating(self):
        print("test_add_rating")
        initial_size = len(self.__rating_service.get_all())
        self.__rating_service.adauga_rating(43, '1970101123456', 3)
        self.assertEqual(len(self.__rating_service.get_all()), initial_size + 1)

        self.assertRaises(RatingAlreadyExistsException, self.__rating_service.adauga_rating, 43,
                          '1970101123456', 3.25)
        self.assertRaises(PersonDoesNotExistException, self.__rating_service.adauga_rating, 1, '1760920213241',
                          5)
        self.assertRaises(SongDoesNotExistException, self.__rating_service.adauga_rating, 1347, '1234567890123',
                          4.5)
        self.assertRaises(ValidationException, self.__rating_service.adauga_rating, 8, '1234567890123', 100)

    def test_most_listened_to_report(self):
        print("test_most_listened_to_report")

        # Based on file content, list should be:
        # Thriller - Michael Jackson, 4 ascultari
        # Hotel California - Eagles, 3 ascultari
        # Smoke on the Water - Deep Purple, 2 ascultari
        # de adaugat: mod de gestionare a cazului in care nu exista suficiente evaluari
        # + teste in acest sens
        dto_list = self.__rating_service.most_listened_to(3)
        self.assertEqual(len(dto_list), 3)

        dto1 = dto_list[0]
        self.assertEqual(dto1.titlu, "Thriller")
        self.assertEqual(dto1.nr_ascultari,4)

        dto1 = dto_list[1]
        self.assertEqual(dto1.titlu, "Hotel California")
        self.assertEqual(dto1.nr_ascultari, 3)

        dto1 = dto_list[2]
        self.assertEqual(dto1.titlu, "Smoke on the Water")
        self.assertEqual(dto1.nr_ascultari, 2)

        dto_list = self.__rating_service.most_listened_to(1)
        self.assertEqual(len(dto_list), 1)

        dto1 = dto_list[0]
        self.assertEqual(dto1.titlu, "Thriller")
        self.assertEqual(dto1.nr_ascultari, 4)

        dto_list = self.__rating_service.most_listened_to(4)
        self.assertEqual(len(dto_list), 4)

        last_dto = dto_list[-1]
        self.assertEqual(last_dto.titlu, "Bad Romance")
        self.assertEqual(last_dto.nr_ascultari, 1)

    def test_song_evaluations_repo(self):
        print("test_song_evaluations")
        pass


    def tearDown(self) -> None:
        print("tearDown called")
        #method that unittest calls after running each test method in a test case class
        clear_file_content("test_melodii.txt")
        clear_file_content("test_persons.txt")
        clear_file_content("test_evaluari.txt")

if __name__ == '__main__':
    unittest.main()