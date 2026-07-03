import unittest
from domain.client import Client
from repository.client_repository import ClientRepository

class TestClientRepository(unittest.TestCase):
    def setUp(self):
        self.repo = ClientRepository()

    def test_store_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        client1 = Client(1, 'Nume', '321321')
        self.repo.store(client1)

        client2 = Client(1, "AltNume", "321321443")
        self.assertRaises(ValueError, self.repo.store, client2)

        self.repo.store(Client(2, "Nume", "3213214"))
        self.assertEqual(self.repo.get_size(), 2)

    def test_update_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        client = Client(1, "Nume", "321321")
        self.assertRaises(ValueError, self.repo.update, client)

        self.repo.store(client)
        self.assertEqual(self.repo.get_size(), 1)

        client2 = Client(1, "AltNume", "321321443")
        self.repo.update(client2)

        self.assertEqual(self.repo.get_size(), 1)
        modified_client = self.repo.find(1)
        self.assertEqual(modified_client.name, "AltNume")
        self.assertEqual(modified_client.cnp, "321321443")

        client3 = Client(2, "Nume", "321415")
        self.assertRaises(ValueError, self.repo.update, client3)
        self.assertEqual(self.repo.get_size(), 1)

    def test_find_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        client_found = self.repo.find(1)
        self.assertIsNone(client_found)

        client1 = Client(1, "Nume", "321321")
        client2 = Client(2, "Nume", "321321443")
        client3 = Client(3, "nume", "321415")

        self.repo.store(client1)
        self.repo.store(client2)
        self.repo.store(client3)

        self.assertEqual(self.repo.get_size(), 3)

        client1_found = self.repo.find(1)
        self.assertIsNotNone(client1_found)

        client2_found = self.repo.find(2)
        self.assertEqual(client2_found.cnp, "321321443")

        client3_found = self.repo.find(3)
        self.assertEqual(client3_found.name, "nume")

        client4_found = self.repo.find(4)
        self.assertIsNone(client4_found)

    def test_delete_repository(self):
        self.assertEqual(self.repo.get_size(), 0)

        client1 = Client(1, "Nume", "321321")
        client2 = Client(2, "Nume", "321321443")
        client3 = Client(3, "nume", "321415")

        self.repo.store(client1)
        self.repo.store(client2)
        self.repo.store(client3)
        self.assertEqual(self.repo.get_size(), 3)

        self.repo.delete(1)
        self.assertEqual(self.repo.get_size(), 2)

        self.assertRaises(ValueError, self.repo.delete, 1)

        self.repo.delete(2)
        self.assertEqual(self.repo.get_size(), 1)


if __name__ == "__main__":
    unittest.main()