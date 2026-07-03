from utils.list_utils import add_to_list, copy_list_of_dicts


def test_add_to_list():
    test_list = []
    assert (len(test_list) == 0)
    add_to_list(test_list, 1)
    assert (len(test_list) == 1)

    test_list.clear()

    add_to_list(test_list, {'key1': 2, 'key2': 3})
    assert (len(test_list) == 1)

    add_to_list(test_list, {'key1': 2, 'key2': 5})
    assert (len(test_list) == 2)

    test_list.clear()

    add_to_list(test_list, [{'key1': 2, 'key2': 3}])
    assert (len(test_list) == 1)

    add_to_list(test_list, [{'key1': 2, 'key2': 3}, {'key1': 2, 'key2': 5}])
    assert (len(test_list) == 2)
    add_to_list(test_list, [{'key1': 2, 'key2': 3}, {'key1': 2, 'key2': 5}, {'key1': 4, 'key2': 20}])
    assert (len(test_list) == 3)


def test_copy_list_of_dicts():
    a = {'key1': 8, 'key2': 1}
    b = {'key1': 2, 'key2': 15}
    c = {'key1': -35, 'key2': 9}
    test_list = [a, b, c]

    test_list_copy = copy_list_of_dicts(test_list)
    assert (test_list_copy == test_list)
    assert (id(test_list) != id(test_list_copy))
    assert (id(test_list_copy[0]) != id(a))
    assert (id(test_list_copy[1]) != id(b))
    assert (id(test_list_copy[2]) != id(c))

    test_list1 = []
    test_list_copy1 = copy_list_of_dicts(test_list1)
    assert (test_list_copy1 == test_list1)
    assert (id(test_list1) != id(test_list_copy1))


test_add_to_list()
test_copy_list_of_dicts()
