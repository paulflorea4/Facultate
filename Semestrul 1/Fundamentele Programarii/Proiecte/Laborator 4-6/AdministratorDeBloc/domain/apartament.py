from datetime import date

def get_sum(data:tuple):
    return data[0]

def get_date(data:tuple):
    return data[1]

def get_cost_sum(costs:dict,cost_type:str):
    """
    Returneaza lista sumelor unei cheltuieli de la un apartament
    :param costs: lista de cheltuieli
    :param cost_type: tipul de cheltuiala
    :return: lista sumelor
    """
    cost_sum=[]
    if cost_type in costs:
        for cost in costs[cost_type]:
            cost_sum.append(get_sum(cost))
    return cost_sum

def create_apartment(apartments:dict,aparment_index:int):
    apartments[aparment_index]={}

def get_apartments_costs(apartments:dict,aparment_index:int):
    return apartments[aparment_index]

def get_cost_type(apartments_costs:dict,cost_type:str):
    return apartments_costs[cost_type]

def get_apartments_numbers(apartments:dict):
    return apartments.keys()

def add_cost_to_apartment(apartment:dict,cost_type:str,cost_value:float,cost_date:date):
    """
    Adauga o cheltuiala pentru un apartament
    :param cost_date:
    :param apartment:apartamentul la care se adauga de tip dictionar
    :param cost_type:tipul cheltuielii
    :param cost_value:suma cheltuielii
    :param cost_date:data cheltuielii
    :return:-
    """
    if cost_type in apartment:
        if (cost_value,cost_date) not in apartment[cost_type]:
            apartment[cost_type].append((cost_value,cost_date))
    else:
        apartment[cost_type] = [(cost_value,cost_date)]

def modify_cost(apartment:list,index:int,new_cost_value:float,new_cost_date:date):
    """
    Modifica suma unui tip de cheltuiala
    :param index:numarul cheltuielii care urmeaza sa fie modificata
    :param apartment:apartamentul la care se modifica cheltuiala
    :param new_cost_value: tipul cheltuielii
    :param new_cost_date: suma cheltuielii
    :return: -
    """
    apartment.pop(index)
    apartment.insert(index,(new_cost_value,new_cost_date))

def print_apartment(apartment:dict,index:int):
    """
    Printeaza cheltuielile de la un apartament dat
    :param apartment: lista de cheltuieli de la apartamentul dat
    :param index: numarul apartamentului
    :return: -
    """
    print(f"Apartamentul {index}:")
    for cost_type in apartment:
        print(f"{cost_type}: ",end='')
        for cost_data in apartment[cost_type]:
            print(f"{get_sum(cost_data)} lei in {get_date(cost_data)}",end=' , ')
        print()

def print_cost(costs:dict,index:int,cost_type:str):
    """
    Printeaza un tip de cheltuiala de la un apartament dat
    :param costs: lista de cheltuieli de la apartamentul dat
    :param index: numarul apartamentului
    :param cost_type: tipul cheltuielii
    :return: -
    """
    print(f"Apartamentul {index}:",end='')
    for cost_data in costs[cost_type]:
        print(f"{get_date(cost_data)}:{get_sum(cost_data)} lei",end=',')
    print()

def delete_all_costs(apartment:dict):
    """
    Sterge toate cheltuielile unui apartament
    :param apartment: apartamentul la care se sterg cheltuielile
    :return:-
    """
    apartment.clear()

def delete_specific_cost(apartment:dict,cost_type:str):
    """
    Sterge un tip de cheltuiala de la un apartament
    :param apartment:apartamentul de la care se sterge cheltuiala
    :param cost_type:tipul cheltuielii
    :return:-
    """
    if cost_type in apartment:
        del apartment[cost_type]

def total_cost(costs:dict):
    """
    Calculeaza suma cheltuielilor unui apartament
    :param costs: lista costurilor unui apartament
    :return: suma cheltuielilor
    """
    total_sum=0
    for cost_type in costs.values():
        for cost_data in cost_type:
            total_sum+=get_sum(cost_data)
    return total_sum

def get_specific_cost(costs:dict,cost_type:str):
    """
    Gasteste suma si data unui tip de cheltuiala de la un apartament
    :param costs: lista de costuri unui apartament
    :param cost_type: tipul cheltuielii
    :return: suma si data cheltuielii daca exista
    """
    return costs.get(cost_type,None)
