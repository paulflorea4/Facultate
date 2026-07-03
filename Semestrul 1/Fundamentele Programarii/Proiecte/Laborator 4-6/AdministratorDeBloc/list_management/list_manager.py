from domain.apartament import *

def add_default_costs(apartments:dict):
    """
    Adauga cheltuieli default unor apartamente
    :param apartments: lista de apartamente
    :return: -
    """
    apartments[0] = {
        "apa": [(190, date(2024, 3, 8)), (210, date(2024, 4, 12))],
        "incalzire": [(230, date(2024, 3, 10)), (310, date(2024, 4, 14))]
    }
    apartments[1] = {
        "gaz": [(150, date(2024, 1, 10)), (220, date(2024, 2, 15))],
        "canal": [(260, date(2024, 1, 12)), (310, date(2024, 2, 20))]
    }
    apartments[2] = {
        "apa": [(120, date(2024, 1, 20)), (180, date(2024, 2, 25))],
        "gaz": [(240, date(2024, 1, 22)), (290, date(2024, 2, 28))]
    }
    apartments[3] = {
        "apa": [(130, date(2024, 1, 15)), (210, date(2024, 2, 18))],
        "incalzire": [(270, date(2024, 1, 17)), (320, date(2024, 2, 22))]
    }
    apartments[4] = {
        "canal": [(160, date(2024, 1, 25)), (240, date(2024, 4, 30))],
        "internet": [(140, date(2024, 3, 12)), (230, date(2024, 1, 10))],
        "gaz": [(280, date(2024, 1, 28)), (330, date(2024, 3, 5))]
    }
    apartments[5] = {
        "internet": [(140, date(2024, 1, 12)), (230, date(2024, 2, 10))],
        "gaz": [(250, date(2024, 1, 14)), (300, date(2024, 2, 18))]
    }
    apartments[9] = {
        "apa": [(200, date(2024, 8, 13)), (230, date(2024, 9, 10))],
        "incalzire": [(185, date(2024, 5, 30)), (250, date(2024, 6, 1))],
        "gaz": [(290, date(2024, 5, 31)), (340, date(2024, 6, 3))]
    }
    apartments[10] = {
        "apa": [(200, date(2024, 5, 5)), (230, date(2024, 6, 10))],
        "gaz": [(260, date(2024, 5, 7)), (300, date(2024, 6, 12))]
    }

def delete_specific_cost_all(apartments:dict,cost_type:str):
    """
    Sterge un tip de cheltuiala de la toate apartamentele
    :param apartments: lista de apartamente
    :param cost_type: tipul cheltuielii
    :return: -
    """
    for costs in apartments.values():
        if cost_type in costs:
            del costs[cost_type]

def costs_greater_than_sum(apartments:dict,price:float):
    """
    Gaseste apartamentele care au costuri mai mari decat o suma data
    :param apartments: lista de apartamente
    :param price:suma data
    :return:numerele apartamentelor care verifica proprietatea
    """
    apartment_list=[]
    for apartment_index in apartments:
        if total_cost(apartments[apartment_index])>price:
            apartment_list.append(apartment_index)
    return apartment_list

def sort_by_cost_type(apartments:dict,cost_type:str):
    """
    Sorteaza crescator apartamentele dupa un tip de cheltuiala
    :param apartments:lista de apartamente cu cheltuieli
    :param cost_type:tipul cheltuielii
    :return:lista de apartamente sortata crescator
    """
    cost_type_total={}
    for apartment_index,costs in apartments.items():
        if cost_type in costs:
            cost_total=sum(get_sum(cost_data) for cost_data in costs[cost_type])
            cost_type_total[apartment_index]=cost_total
        else:
            cost_type_total[apartment_index]=None

    return sorted(cost_type_total.keys(),key=lambda x:(cost_type_total[x] is None,cost_type_total[x]))


def filter_cost_list_type(apartments:dict,cost_type:str):
    """
    Elimina cheltuielile de un anumit tip
    :param apartments:lista de apartamente
    :param cost_type:tipul cheltuielii
    :return:lista cu cheltuielile apartamentelor fara tipul de cheltuiala dat
    """
    filtered_apartments={}
    for apartment_index,costs in apartments.items():
        filtered_costs={key:value for key,value in costs.items()}
        if cost_type in filtered_costs:
            filtered_costs.pop(cost_type)
        if len(filtered_costs)>0:
            filtered_apartments[apartment_index]=filtered_costs
    return filtered_apartments

def filter_cost_list_sum(apartments:dict,sum:float):
    """
    Elimina cheltuielile mai mici decat o suma data
    :param apartments:lista de apartamente
    :param sum:suma data
    :return:lista de apartamente cu cheltuieli mai mari decat suma data
    """
    filtered_apartments={}
    for apartment_index,costs in apartments.items():
        filtered_costs={}
        for cost_type,cost_data in costs.items():
            filtered_data=[]
            for cost in cost_data:
                if get_sum(cost)>=sum:
                    filtered_data.append(cost)
            if len(filtered_data)>0:
                filtered_costs[cost_type]=filtered_data
        if len(filtered_costs)>0:
            filtered_apartments[apartment_index]=filtered_costs
    return filtered_apartments

def costs_date_limit_sum(costs:dict,sum:float,date_limit:date):
    """
    Cauta toate cheltuielile efectuate inainte de o data si mai mari decat o suma data
    :param costs:lista de cheltuieli
    :param sum:suma data
    :param date_limit:data
    :return:lista de cheltuieli gasite
    """
    list={}
    for cost_type,cost_data in costs.items():
        for cost in cost_data:
            if get_date(cost)<date_limit and get_sum(cost)>=sum:
                if cost_type in list:
                    list[cost_type].append(cost)
                else:
                    list[cost_type] = [cost]
    return list

def undo(cost_list:dict,undo_list:list):
    if len(undo_list)==0:
        raise ValueError("Nu se mai poate face undo.")
    cost_list.clear()
    cost_list.update(undo_list.pop())


