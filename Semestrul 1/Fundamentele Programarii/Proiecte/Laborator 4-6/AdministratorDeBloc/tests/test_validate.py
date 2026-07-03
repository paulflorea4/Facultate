from domain.validate import *

def test_validate_cost():
    try:
        validate_cost("apa",-100)
        assert False
    except ValueError:
        assert True

    try:
        validate_cost("z",-21)
        assert False
    except ValueError:
        assert True

    try:
        validate_cost("a",100)
        assert False
    except ValueError:
        assert True