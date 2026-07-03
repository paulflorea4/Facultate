import unittest
from domain.validation import MovieValidation
from repository.movie_repository import MovieRepository
from service.movie_controller import ControllerMovie

class TestControllerMovie(unittest.TestCase):
    def setUp(self):
        self.repo = MovieRepository()
        self.validation = MovieValidation()
        self.service = ControllerMovie(self.repo, self.validation)

    def test_add_movie(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.service.add_movie(1, "Batman", "Descriere", "Actiune")
        self.assertEqual(len(self.service.get_all()), 1)

        self.assertRaises(ValueError, self.service.add_movie, 1, "Spider-Man", "Descriere", "Actiune")

        self.assertRaises(ValueError, self.service.add_movie, 2, "", "Descriere2", "Horror")

        self.assertRaises(ValueError, self.service.add_movie, 3, "Aquaman", "Descriere3", "")

    def test_update_movie(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.service.add_movie(1, "Titlu1", "Descriere1", "Actiune")
        self.service.add_movie(2, "Titlu2", "Descriere2", "Horror")
        self.service.add_movie(3, "Titlu3", "Descriere3", "Comedie")
        self.assertEqual(len(self.service.get_all()), 3)

        self.service.update_movie(1, "Titlu4", "Descriere4", "Drama")
        updated_movie = self.service.find_movie(1)
        self.assertEqual(updated_movie.title, "Titlu4")
        self.assertEqual(updated_movie.description, "Descriere4")
        self.assertEqual(updated_movie.type, "Drama")

        self.assertRaises(ValueError, self.service.update_movie, 5, "Titlu5", "Descriere5", "Actiune")

        self.assertRaises(ValueError, self.service.update_movie, 2, "", "Descriere2", "Horror")

        self.assertRaises(ValueError, self.service.update_movie, 3, "Titlu3", "Descriere3", "")

    def test_find_movie(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.assertIsNone(self.service.find_movie(1))

        self.service.add_movie(1, "Titlu1", "Descriere1", "Actiune")
        self.assertIsNotNone(self.service.find_movie(1))

    def test_delete_movie(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.assertRaises(ValueError, self.service.delete_movie, 1)

        self.service.add_movie(1, "Titlu1", "Descriere1", "Actiune")
        self.assertEqual(len(self.service.get_all()), 1)
        self.service.delete_movie(1)
        self.assertEqual(len(self.service.get_all()), 0)


if __name__ == "__main__":
    unittest.main()