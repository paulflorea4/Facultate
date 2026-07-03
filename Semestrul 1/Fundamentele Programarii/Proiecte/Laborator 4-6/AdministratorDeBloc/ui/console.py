from list_management.list_manager import *
from domain.validate import *
from utils.list_utils import *
from tests.test_run import test

def print_menu():
    print("1. Adaugare")
    print("2. Stergere")
    print("3. Cautari")
    print("4. Rapoarte")
    print("5. Filtru")
    print("D. Default")
    print("U. Undo")
    print("P. Afisare")
    print("E. Exit")

def add_options():
    print("a. Adauga cheltuiala pentru un apartament")
    print("b. Modifica cheltuiala")
    print("e. Iesire")

def delete_options():
    print("a. Sterge toate cheltuielile de la un apartament")
    print("b. Sterge cheltuielile de la apartamente consecutive")
    print("c. Sterge cheltuielile de un anumit tip de la toate apartamentele")
    print("e. Iesire")

def print_options():
    print("a.Afiseaza cheltuielile de la un apartament")
    print("b.Afiseaza toate cheltuielile")
    print("e. Iesire")

def search_options():
    print("a. Tipareste toate apartamentele care au cheltuieli mai mari decat o suma data")
    print("b. Tipareste toate cheltuielile de un anumit tip de la toate apartamentele")
    print("c. Tipareste toate cheltuiele efectuate inainte de o zi si mai mari decat o suma")
    print("e. Iesire")

def costs_options():
    print("a. Tipareste suma totala pentru un tip de cheltuiala")
    print("b. Tipareste toate apartamentele sortate dupa un tip de cheltuiala")
    print("c. Tipareste totalul de cheltuieli pentru un apartament dat")
    print("e. Iesire")

def filter_options():
    print("a. Elimina toate cheltuielile de un anumit tip")
    print("b. Elimina toate cheltuielile mai mici decat o suma data")
    print("e. Iesire")

def modify_options(apartment:list):
    for index,cost in enumerate(apartment):
        cost_value = get_sum(cost)
        cost_date=get_date(cost)
        print(f"{index}.{cost_date}: {cost_value} lei")
    print("Introduceti numarul cheltuielii pe care doriti sa o schimbati:",end='')

def cost_input():
    """
    Citeste datele unei cheltuieli
    :return: tipul, suma si data cheltuielii
    """
    cost_type = input("Tipul cheltuielii:").lower().strip()
    cost_value = convert_to_float(input("Suma Cheltuielii:").strip())
    year=int(input("An:").strip())
    month=int(input("Luna:").strip())
    day=int(input("Zi:").strip())
    cost_date=date(year,month,day)
    validate_cost(cost_type, cost_value)
    return cost_type, cost_value, cost_date

def new_cost_input():
    command_line=input("Introduceti comanda:").strip()
    command_split=command_line.split()
    if command_split[0]=="add":
        option=command_split[0]
        apartment_index=int(command_split[1])
        if command_split[2] in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
            cost_type = command_split[2]
        else:
            cost_type=' '
        cost_value=int(command_split[3])
        year=int(command_split[4])
        month=int(command_split[5])
        day=int(command_split[6])
        cost_date=date(year,month,day)
        validate_cost(cost_type, cost_value)
        return option,apartment_index,cost_type,cost_value,cost_date
    if command_split[0]=="del":
        option=command_split[0]
        apartment_index=int(command_split[1])
        return option,apartment_index
    if command_split[0]=="search":
        option=command_split[0]
        cost_type=command_split[1]
        return option,cost_type
    if command_split[0]=="sort":
        option=command_split[0]
        cost_type=command_split[1]
        return option,cost_type
    if command_split[0]=="filter":
        option=command_split[0]
        cost_type=command_split[1]
        return option,cost_type
    if command_split[0]=="undo":
        option=command_split[0]
        return option
    return command_line

def modify_cost_input():
    """
    Citeste datele noi cheltuieli
    :return: suma si data cheltuielii
    """
    cost_value=convert_to_float(input("Suma cheltuielii:").strip())
    year=int(input("An:").strip())
    month=int(input("Luna:").strip())
    day=int(input("Zi:").strip())
    cost_date=date(year,month,day)
    return cost_value, cost_date

def run():
    test()
    cost_list= {}
    undo_list=[]
    is_running=True
    while is_running:
        print_menu()
        user_input=new_cost_input()
        ok=0
        if user_input[0]=='add':
            option='1'
            option2='a'
            ok=1
        elif user_input[0]=='del':
            option='2'
            option2='a'
            ok=1
        elif user_input[0]=='search':
            option='3'
            option2='b'
            ok=1
        elif user_input[0]=='sort':
            option='4'
            option2='b'
            ok=1
        elif user_input[0]=='filter':
            option='5'
            option2='a'
            ok=1
        elif user_input[0]=='undo':
            option='U'
        else:
            option=user_input.upper()
        match option:
            case "1":
                #ADAUGARE
                if ok==0:
                    add_options()
                    option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        if ok==1:
                            apartment_index,cost_type,cost_value,cost_date=user_input[1],user_input[2],user_input[3],user_input[4]
                        else:
                            apartment_index = convert_to_int((input("Introduceti apartamentul:")))
                            cost_type,cost_value,cost_date=cost_input()
                        undo_list.append(copy_dict(cost_list))
                        if apartment_index not in cost_list:
                            create_apartment(cost_list,apartment_index)
                        add_cost_to_apartment(get_apartments_costs(cost_list,apartment_index),cost_type,cost_value,cost_date)
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="b":
                    try:
                        apartment_index=convert_to_int(input("Introduceti apartamentul:"))
                        cost_type=input("Introduceti tipul cheltuielii:").strip()
                        apartments_costs=get_apartments_costs(cost_list,apartment_index)
                        if cost_type in apartments_costs:
                            modify_options(get_cost_type(apartments_costs, cost_type))
                            cost_index=convert_to_int(input().lower().strip())
                            if cost_index<=len(cost_list[apartment_index][cost_type])-1:
                                new_cost_value,new_cost_date=modify_cost_input()
                                validate_cost(cost_type,new_cost_value)
                                undo_list.append(copy_dict(cost_list))
                                modify_cost(cost_list[apartment_index][cost_type],cost_index,new_cost_value,new_cost_date)
                            else:
                                print("Optiune invalida", end='\n\n')
                        else:
                            print(f"Apartamentul {apartment_index} nu are cheltuieli la {cost_type}",end='\n\n')
                    except KeyError:
                        print(f"Apartamentul {apartment_index} nu exista")
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida",end='\n\n')
            case "2":
                #STERGERE
                if ok==0:
                    delete_options()
                    option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        if ok==1:
                            apartment_index=user_input[1]
                        else:
                            apartment_index = convert_to_int(input("Introduceti apartamentul:"))
                        undo_list.append(copy_dict(cost_list))
                        #delete_all_costs(cost_list[apartment_index])
                        del cost_list[apartment_index]
                    except KeyError:
                        print(f"Apartamentul {apartment_index} nu exista",end='\n\n')
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="b":
                    try:
                        apartment_index_start=convert_to_int(input("Introduceti primul apartament:"))
                        apartment_index_end=convert_to_int(input("Introduceti al doilea apartament:"))
                        if apartment_index_start in get_apartments_numbers(cost_list):
                            undo_list.append(copy_dict(cost_list))
                            for apartment_index in range(apartment_index_start,apartment_index_end+1):
                                if apartment_index in cost_list:
                                    #delete_all_costs(cost_list[apartment_index])
                                    del cost_list[apartment_index]
                                else:
                                    break
                            while True:
                                if apartment_index_start-1 in cost_list:
                                    #delete_all_costs(cost_list[apartment_index_start-1])
                                    del cost_list[apartment_index_start-1]
                                else:
                                    break
                                apartment_index_start-=1
                        else:
                            print(f"Apartamentul {apartment_index} nu exista")
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="c":
                    try:
                        cost_type=input("Tipul cheltuielii:").lower().strip()
                        if cost_type not in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
                            print("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet",end='\n\n')
                        else:
                            undo_list.append(copy_dict(cost_list))
                            delete_specific_cost_all(cost_list,cost_type)
                    except ValueError:
                        print("Valoare invalida",end='\n\n')
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida",end='\n\n')
            case "3":
                #CAUTARE
                if ok==0:
                    search_options()
                    option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        price=convert_to_float(input("Introduceti suma:").strip())
                        if price>0:
                            apartment_list=costs_greater_than_sum(cost_list,price)
                            for apartment_index in apartment_list:
                                print(f"Apartamentul {apartment_index}:{total_cost(cost_list[apartment_index])}")
                        else:
                            print("Suma trebuie sa fie mai mare ca 0",end='\n\n')
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="b":
                    try:
                        if ok==1:
                            cost_type=user_input[1]
                        else:
                            cost_type=input("Tipul cheltuielii:").lower().strip()
                        if cost_type not in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
                            print("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet",end='\n\n')
                        else:
                            for apartment_index in cost_list:
                                if get_specific_cost(cost_list[apartment_index],cost_type) is not None:
                                    print_cost(cost_list[apartment_index],apartment_index,cost_type)
                                else:
                                    print(f"Apartamentul {apartment_index} nu are cheltuieli la {cost_type}")
                    except ValueError:
                        print("Valoare invalida",end='\n\n')
                elif option2=="c":
                        try:
                            price=convert_to_float(input("Introduceti suma:").strip())
                            year=int(input("An:").strip())
                            month=int(input("Luna:").strip())
                            day=int(input("Zi:").strip())
                            date_limit=date(year,month,day)
                            found=0
                            if price>0:
                                for apartment_index in cost_list:
                                    costs=costs_date_limit_sum(get_apartments_costs(cost_list,apartment_index),price,date_limit)
                                    print(costs)
                                    if len(costs)>0:
                                        found=1
                                        print_apartment(costs,apartment_index)
                                        print()
                                if found==0:
                                    print(f"Nu exista cheltuieli efectuate efectuate inainte de {date_limit} si mai mari decat {price}",end='\n\n')
                            else:
                                print("Suma trebuie sa fie mai mare ca 0",end='\n\n')
                        except ValueError as error_message:
                            print(error_message,end='\n\n')
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida",end='\n\n')
            case "4":
                #RAPOARTE
                if ok==0:
                     costs_options()
                     option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        cost_type=input("Tipul cheltuielii:").lower().strip()
                        cost_total=0
                        if cost_type not in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
                            print("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet",end='\n\n')
                        else:
                            for apartment_index in cost_list:
                                cost_sum=get_cost_sum(get_apartments_costs(cost_list,apartment_index),cost_type)
                                cost_total+=sum(cost_sum)
                            print(f"Suma totala pentru {cost_type} este {cost_total}")
                    except ValueError:
                        print("Valoare invalida",end='\n\n')
                elif option2=="b":
                    try:
                        if ok==1:
                            cost_type=user_input[1]
                        else:
                            cost_type=input("Tipul cheltuielii:").lower().strip()
                        if cost_type not in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
                            print("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet",end='\n\n')
                        else:
                            sorted_list=sort_by_cost_type(cost_list,cost_type)
                            for apartment_index in sorted_list:
                                apartment_costs=get_apartments_costs(cost_list,apartment_index)
                                if get_specific_cost(apartment_costs, cost_type) is not None:
                                    costs = get_specific_cost(apartment_costs, cost_type)
                                    print(f"Apartamentul {apartment_index}:{sum(get_sum(cost_data) for cost_data in costs)} lei")
                                else:
                                    print(f"Apartamentul {apartment_index} nu are cheltuieli la {cost_type}")
                    except ValueError:
                        print("Valoare invalida",end='\n\n')
                elif option2=="c":
                    try:
                        apartment_index=convert_to_int(input("Introduceti apartamentul:"))
                        print(f"Apartamentul {apartment_index}:{total_cost(cost_list[apartment_index])} lei")
                    except KeyError:
                        print(f"Apartamentul {apartment_index} nu exista",end='\n\n')
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida",end='\n\n')
            case "5":
                #FILTRU
                if ok==0:
                    filter_options()
                    option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        if ok==1:
                            cost_type=user_input[1]
                        else:
                            cost_type=input("Tipul cheltuielii:").lower().strip()
                        if cost_type not in ['apa', 'canal', 'incalzire', 'gaz', 'internet']:
                            print("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet",end='\n\n')
                        else:
                            filtered_list=filter_cost_list_type(cost_list,cost_type)
                            for apartment_index, apartment_costs in filtered_list.items():
                                print_apartment(apartment_costs, apartment_index)
                                print()
                    except ValueError:
                        print("Valoare invalida",end='\n\n')
                elif option2=="b":
                    try:
                        price=float(input("Introduceti suma:").strip())
                        if price>0:
                            filtered_list=filter_cost_list_sum(cost_list,price)
                            for apartment_index, apartment_costs in filtered_list.items():
                                print_apartment(apartment_costs, apartment_index)
                                print()
                        else:
                            print("Suma trebuie sa fie mai mare ca 0",end='\n\n')
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida",end='\n\n')
            case "D":
                #DEFAULT
                undo_list.append(copy_dict(cost_list))
                add_default_costs(cost_list)
            case "U":
                #UNDO
                try:
                    undo(cost_list,undo_list)
                except ValueError as error_message:
                    print(error_message,end='\n\n')
            case "P":
                #AFISARE
                print_options()
                option2=input(">>>").lower().strip()
                if option2=="a":
                    try:
                        apartment_index=convert_to_int(input("Introduceti apartamentul:"))
                        print_apartment(get_apartments_costs(cost_list,apartment_index),apartment_index)
                    except KeyError:
                        print(f"Apartamentul {apartment_index} nu exista")
                    except ValueError as error_message:
                        print(error_message,end='\n\n')
                elif option2=="b":
                    if len(cost_list)==0:
                        print("Nu exista cheltuieli",end='\n\n')
                    else:
                        for apartment_index,apartment_costs in cost_list.items():
                            print_apartment(apartment_costs,apartment_index)
                            print()
                elif option2=="e":
                    pass
                else:
                    print("Optiune invalida")
            case "E":
                #EXIT
                print("Exiting...")
                is_running=False
            case _:
                print("Optiune invalida",end='\n\n')