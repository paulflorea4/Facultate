import unittest
from domain.fighter import Fighter
from service.FigherController import FigherController
from repository.FighterRepository import FighterRepository

class TestService(unittest.TestCase):
    def setUp(self):
        repository = FighterRepository("Test_fighters")
        repository.store(Fighter(1,"knight",90,25,"Black Army"))
        repository.store(Fighter(2,"archer",41,20,"Red Army"))
        repository.store(Fighter(3,"catapult",85,55,"Black Army"))
        repository.store(Fighter(4,"archer",100,25,"Black Army"))
        repository.store(Fighter(5,"knight",100,22,"Red Army"))
        repository.store(Fighter(6,"knight",80,24,"Red Army"))
        self.service=FigherController(repository)

    def tearDown(self):
        repository=FighterRepository("Test_fighters")
        repository.clear_file()
        self.service=repository

