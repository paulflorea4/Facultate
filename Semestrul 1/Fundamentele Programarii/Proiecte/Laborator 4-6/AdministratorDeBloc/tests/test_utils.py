from domain.apartament import *
from utils.list_utils import *

def test_copy_dict():
    test_list = {
        1: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        4: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        5: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    test_list_copy=copy_dict(test_list)
    assert test_list_copy==test_list
    assert id(test_list)!=id(test_list_copy)

    test_list = {}
    test_list_copy = copy_dict(test_list)
    assert id(test_list)!=id(test_list_copy)
    assert test_list_copy==test_list

def test_convert_to_int():
    try:
        print(convert_to_int("a"))
        assert False
    except ValueError:
        assert True

    try:
        print(convert_to_int(''))
        assert False
    except ValueError:
        assert True


def test_convert_to_float():
    try:
        print(convert_to_int("a"))
        assert False
    except ValueError:
        assert True

    try:
        print(convert_to_int(''))
        assert False
    except ValueError:
        assert True