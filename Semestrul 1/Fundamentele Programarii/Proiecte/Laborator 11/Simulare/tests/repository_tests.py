import unittest

from domain.fighter import Fighter
from repository.FighterRepository import FighterRepository

class TestRepository(unittest.TestCase):
    def setUp(self):
        self.repository = FighterRepository("Test_fighters")
        self.fighter1=Fighter(1,"knight",90,25,"Black Army")
        self.fighter2=Fighter(2,"archer",75,40,"Red Army")
        self.fighter3=Fighter(3,"catapult",55,40,"Black Army")
        self.repository.store(self.fighter1)
        self.repository.store(self.fighter2)
        self.repository.store(self.fighter3)
        self.fighter4=Fighter(4,"archer",75,40,"Black Army")
        self.fighter5=Fighter(3,"knight",100,35,"Black Army")

    def test_find(self):
        self.assertEqual(self.repository.find(1),self.fighter1)
        self.assertEqual(self.repository.find(2),self.fighter2)
        self.assertEqual(self.repository.find(4),None)

    def test_store(self):
        expected_list=[self.fighter1,self.fighter2,self.fighter3,self.fighter4]
        self.repository.store(self.fighter4)
        self.assertEqual(expected_list,self.repository.get_all())
        self.assertRaises(ValueError,self.repository.store,self.fighter5)

    def tearDown(self):
        self.repository.clear_file()