import unittest
from domain.movie import Movie
from repository.movie_repository import MovieRepository

class TestMovieRepository(unittest.TestCase):
    def setUp(self):
        self.repo = MovieRepository()

    def test_store_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        self.repo.store(movie1)

        movie2 = Movie(1, "Titlu2", "Descriere2", "Horror")
        self.assertRaises(ValueError, self.repo.store, movie2)

        self.repo.store(Movie(2, "Titlu3", "Descriere3", "Comedie"))
        self.assertEqual(self.repo.get_size(), 2)

    def test_update_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        self.assertRaises(ValueError, self.repo.update, movie1)

        self.repo.store(movie1)
        self.assertEqual(self.repo.get_size(), 1)

        movie2 = Movie(1, "Titlu2", "Descriere2", "Horror")
        self.repo.update(movie2)

        self.assertEqual(self.repo.get_size(), 1)
        modified_movie = self.repo.find(1)
        self.assertEqual(modified_movie.title, "Titlu2")
        self.assertEqual(modified_movie.description, "Descriere2")
        self.assertEqual(modified_movie.type, "Horror")

        movie3 = Movie(2, "Titlu3", "Descriere3", "Comedie")
        self.assertRaises(ValueError, self.repo.update, movie3)
        self.assertEqual(self.repo.get_size(), 1)

    def test_find_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        movie_found = self.repo.find(1)
        self.assertIsNone(movie_found)

        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        movie2 = Movie(2, "Titlu2", "Descriere2", "Horror")
        movie3 = Movie(3, "Titlu3", "Descriere3", "Comedie")

        self.repo.store(movie1)
        self.repo.store(movie2)
        self.repo.store(movie3)

        self.assertEqual(self.repo.get_size(), 3)

        movie1_found = self.repo.find(1)
        self.assertIsNotNone(movie1_found)

        movie2_found = self.repo.find(2)
        self.assertEqual(movie2_found.title, "Titlu2")

        movie3_found = self.repo.find(3)
        self.assertEqual(movie3_found.type, "Comedie")

        movie4_found = self.repo.find(4)
        self.assertIsNone(movie4_found)

    def test_delete_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        movie2 = Movie(2, "Titlu2", "Descriere2", "Horror")
        movie3 = Movie(3, "Titlu3", "Descriere3", "Comedie")

        self.repo.store(movie1)
        self.repo.store(movie2)
        self.repo.store(movie3)
        self.assertEqual(self.repo.get_size(), 3)

        self.repo.delete(1)
        self.assertEqual(self.repo.get_size(), 2)

        self.assertRaises(ValueError, self.repo.delete, 1)

        self.repo.delete(2)
        self.assertEqual(self.repo.get_size(), 1)


if __name__ == "__main__":
    unittest.main()