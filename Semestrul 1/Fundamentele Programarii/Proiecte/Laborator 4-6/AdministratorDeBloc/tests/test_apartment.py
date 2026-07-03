from domain.apartament import *

def test_add_cost_to_apartament():
    test_apartment={}
    assert (len(test_apartment)==0)
    add_cost_to_apartment(test_apartment,"apa",100,date(2024,7,8))
    assert (len(test_apartment)==1)
    add_cost_to_apartment(test_apartment,"gaz",200,date(2024,5,12))
    assert (len(test_apartment)==2)

def test_modify_cost():
    test_apartment={}
    add_cost_to_apartment(test_apartment,"apa",100,date(2024,7,8))
    modify_cost(test_apartment["apa"],0,200,date(2024,5,12))
    assert get_sum(test_apartment["apa"][0])==200

def test_delete_specific_cost():
    test_apartment={"apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
            "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]}
    delete_specific_cost(test_apartment,"canal")
    assert (len(test_apartment)==2)
    delete_specific_cost(test_apartment,"incalzire")
    assert (len(test_apartment)==1)

def test_total_cost():
    test_apartment={}
    assert (total_cost(test_apartment)==0)
    test_apartment={"apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
            "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]}
    assert (total_cost(test_apartment)==940)

def test_get_specific_cost():
    test_apartment={"apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
            "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]}
    assert (get_specific_cost(test_apartment,"canal") is None)
    assert (get_specific_cost(test_apartment,"incalzire")==[(230, date(2024, 3, 10)), (310, date(2024, 4, 14))])
    assert (get_specific_cost(test_apartment,"internet") is None)