from domain.client import Client
from domain.validation import ClientValidation
import unittest

class TestClient(unittest.TestCase):

    def setUp(self):
        self.client = Client(1, "Nume", "12323")

    def test_client(self):
        self.assertEqual(self.client.get_id(), 1)
        self.assertEqual(self.client.name, "Nume")
        self.assertEqual(self.client.cnp, "12323")

        self.client.name = "AltNume"
        self.assertEqual(self.client.name, "AltNume")

        self.client.cnp = "98710987"
        self.assertEqual(self.client.cnp, "98710987")

class TestClientValidation(unittest.TestCase):

    def setUp(self):
        self.validator = ClientValidation()

    def test_valid_client(self):
        valid_client = Client(1, "Nume", "123456")
        self.validator.validate(valid_client)

    def test_invalid_client(self):
        client = Client(1, "", "123456")
        self.assertRaises(ValueError, self.validator.validate, client)

        client = Client(1, "Nume", "123A45")
        self.assertRaises(ValueError, self.validator.validate, client)

        client = Client(1, "Nume", "12")
        self.assertRaises(ValueError, self.validator.validate, client)

        client = Client(1, "Nume123 ", "123456")
        self.assertRaises(ValueError, self.validator.validate, client)

        client = Client(1, "", "1A")
        self.assertRaises(ValueError, self.validator.validate, client)

if __name__ == "__main__":
    unittest.main()
