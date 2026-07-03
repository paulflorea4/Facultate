from domain.movie import Movie
from domain.validation import MovieValidation
import unittest

class TestMovie(unittest.TestCase):

    def setUp(self):
        self.movie = Movie(1, "Titlu", "Descriere", "Drama")

    def test_movie(self):
        self.assertEqual(self.movie.get_id(), 1)
        self.assertEqual(self.movie.title, "Titlu")
        self.assertEqual(self.movie.description, "Descriere")
        self.assertEqual(self.movie.type, "Drama")

        self.movie.title = "Titlu2"
        self.assertEqual(self.movie.title, "Titlu2")

        self.movie.description = "Descriere2"
        self.assertEqual(self.movie.description, "Descriere2")

        self.movie.type = "Actiune"
        self.assertEqual(self.movie.type, "Actiune")

        self.assertEqual(
            str(self.movie),
            "[1] Film: Titlu = Titlu2, Descriere = Descriere2, Gen = Actiune"
        )

class TestMovieValidation(unittest.TestCase):

    def setUp(self):
        self.validator = MovieValidation()

    def test_valid_movie(self):
        valid_movie = Movie(1, "Titlu", "Descriere", "Drama")
        self.validator.validate(valid_movie)

    def test_invalid_movie(self):
        movie = Movie(1, "", "Descriere", "Drama")
        self.assertRaises(ValueError, self.validator.validate, movie)

        movie = Movie(2, "Titlu", "", "Drama")
        self.assertRaises(ValueError, self.validator.validate, movie)

        movie = Movie(3, "Titlu", "Descriere", "Animatie")
        self.assertRaises(ValueError, self.validator.validate, movie)

        movie = Movie(4, "", "", "Documentar")
        self.assertRaises(ValueError, self.validator.validate, movie)

if __name__ == "__main__":
    unittest.main()


