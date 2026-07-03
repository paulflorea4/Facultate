import unittest
from repository.rent_repository import RentRepository
from domain.rent import Rent
from domain.movie import Movie
from domain.client import Client

class TestRentRepository(unittest.TestCase):
    def setUp(self):
        self.repo = RentRepository()
        self.assertEqual(self.repo.get_size(), 0)

    def test_store_repository(self):
        client1 = Client(1, 'Nume', '321321')
        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        rent1 = Rent(client1, movie1)

        self.repo.store(rent1)
        self.assertEqual(self.repo.get_size(), 1)

        self.assertRaises(ValueError, self.repo.store, rent1)

        self.assertEqual(self.repo.get_size(), 1)

    def test_find_repository(self):
        client1 = Client(1, 'Nume', '321321')
        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        rent1 = Rent(client1, movie1)

        rent_found = self.repo.find(rent1)
        self.assertIsNone(rent_found)

        self.repo.store(rent1)
        self.assertEqual(self.repo.get_size(), 1)

        rent_found = self.repo.find(rent1)
        self.assertIsNotNone(rent_found)

    def test_delete_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        client1 = Client(1, 'Nume', '321321')
        movie1 = Movie(1, "Titlu", "Descriere", "Actiune")
        rent1 = Rent(client1, movie1)

        self.assertRaises(ValueError, self.repo.delete, rent1)

        self.repo.store(rent1)
        self.assertEqual(self.repo.get_size(), 1)

        self.repo.delete(rent1)
        self.assertEqual(self.repo.get_size(), 0)


if __name__ == "__main__":
    unittest.main()