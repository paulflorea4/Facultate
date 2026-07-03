from list_management.list_manager import *
from utils.list_utils import copy_dict


def test_delete_all_costs():
    test_apartment={"apa":[(100,date(2024,10,19)),(200,date(2023,12,28))],"gaz":[(250,date(2024,11,21))]}
    assert (len(test_apartment)==2)
    delete_all_costs(test_apartment)
    assert (len(test_apartment)==0)

def test_delete_specific_cost_all():
    test_list={
        1:{"apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
            "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]},

        6:{"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(260, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        13: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    delete_specific_cost_all(test_list,"apa")
    assert (len(test_list[1])==1)
    assert (len(test_list[6])==2)
    assert (len(test_list[13])==1)

def test_costs_greater_than_sum():
    test_list = {
        1: {"apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
            "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]},

        7: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        21: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    apartment_list=costs_greater_than_sum(test_list,800)
    assert (len(apartment_list)==2)
    apartment_list=costs_greater_than_sum(test_list,1000)
    assert (len(apartment_list)==0)

def test_sort_by_cost_type():
    test_list = {
        3:{"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        7: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        21: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    assert ((sort_by_cost_type(test_list,"gaz"))==[7,21,3])

def test_add_default_costs():
    test_list={}
    add_default_costs(test_list)
    assert (len(test_list[0])==2)
    assert (len(test_list[4])==3)
    assert (len(test_list[9])==3)

def test_filter_cost_list():
    test_list = {
        3: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        7: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        13: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    filtered_test_list=filter_cost_list_type(test_list,"apa")
    assert (filtered_test_list=={3: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},7: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},13: {"gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}})

def test_filter_cost_list_sum():
    test_list = {
        3: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        4: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        13: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    filtered_test_list=filter_cost_list_sum(test_list,300)
    assert filtered_test_list=={3:{"gaz": [ (300, date(2024, 2, 18))]},4:{"canal": [(310, date(2024, 2, 20))]}}
    filtered_test_list=filter_cost_list_sum(test_list,500)
    assert filtered_test_list=={}

def test_costs_date_limit_sum():
    test_list = {
        1: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        4: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        13: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    assert costs_date_limit_sum(test_list[1],300,date(2024,1,12))=={}
    assert costs_date_limit_sum(test_list[4],100,date(2024,1,13))=={"gaz":[(150, date(2024, 1, 10))],"canal": [(100, date(2024, 1, 12))]}

def test_undo():
    undo_list=[]
    test_list = {
        1: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        4: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        7: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }
    undo_list.append(copy_dict(test_list))
    test_list[8]={"apa":[(100, date(2024, 1, 12))],"gaz":[(180, date(2024, 2, 25))]}
    undo(test_list,undo_list)
    assert test_list=={
        1: {"internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
            "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]},

        4: {"gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
            "canal": [(100, date(2024, 1, 12)), (310, date(2024, 2, 20))]},

        7: {"apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
             "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]}
    }


