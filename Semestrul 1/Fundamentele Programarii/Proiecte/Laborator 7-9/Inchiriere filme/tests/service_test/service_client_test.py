import unittest
from domain.validation import ClientValidation
from repository.client_repository import ClientRepository
from service.client_controller import ControllerClient

class TestControllerClient(unittest.TestCase):
    def setUp(self):
        self.repo = ClientRepository()
        self.validation = ClientValidation()
        self.service = ControllerClient(self.repo, self.validation)

    def test_add_client(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.service.add_client(1, "Nume", "3214421")
        self.assertEqual(len(self.service.get_all()), 1)

        self.assertRaises(ValueError, self.service.add_client, 1, "AltNume", "321441421")

        self.assertRaises(ValueError, self.service.add_client, 2, "", "14214421")

        self.assertRaises(ValueError,self.service.add_client,3, "NuMe", "32")

    def test_update_client(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.service.add_client(1, "Alex", "3214421")
        self.service.add_client(2, "Cristian", "3214434421")
        self.service.add_client(3, "Andrei", "321441421")
        self.assertEqual(len(self.service.get_all()), 3)

        self.service.update_client(1, "Tudor", "1234")
        updated_client = self.service.find_client(1)
        self.assertEqual(updated_client.name, "Tudor")

        self.assertRaises(ValueError,self.service.update_client,2, "", "1234")

        self.assertRaises(ValueError,self.service.update_client,4, "Ion", "123421")

        self.assertRaises(ValueError,self.service.update_client,3, "Stefan", "1")

    def test_find_client(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.assertIsNone(self.service.find_client(1))

        self.service.add_client(1, "Alex", "3214421")
        self.assertEqual(len(self.service.get_all()), 1)
        client_found = self.service.find_client(1)
        self.assertEqual(client_found.name, "Alex")

        self.service.add_client(2, "Cristian", "123457")
        self.assertEqual(len(self.service.get_all()), 2)
        self.assertIsNotNone(self.service.find_client(2))

    def test_delete_client(self):
        self.assertEqual(len(self.service.get_all()), 0)

        self.assertRaises(ValueError, self.service.delete_client, 1)

        self.service.add_client(1, "Alex", "3214421")
        self.assertEqual(len(self.service.get_all()), 1)

        self.service.delete_client(1)
        self.assertEqual(len(self.service.get_all()), 0)


if __name__ == "__main__":
    unittest.main()