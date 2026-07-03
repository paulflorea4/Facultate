import unittest

from domain.fighter import Fighter

class TestFighter(unittest.TestCase):
    def setUp(self):
        self.fighter = Fighter(1,"knight",95,30,"Red Army")

    def test_fighter(self):
        self.assertEqual(self.fighter.get_id(),1)
        self.assertEqual(self.fighter.get_unit_type(),"knight")
        self.assertEqual(self.fighter.get_health_points(),95)
        self.assertEqual(self.fighter.get_attack_points(),30)
        self.assertEqual(self.fighter.get_army(),"Red Army")