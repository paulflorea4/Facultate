#I1:F1:Citire lista numere
#I1:F2:Adaugare numar la lista
#I1:F6:Iesire din aplicatie

def afiseaza_meniu():
    print("1.Citire lista numere")
    print("2Adaugare numar la lista")
    print("E.Exist")

def transforma_in_lista(list_string):
    lista_stringuri=list_string.split(',')
    lista_ints=[]
    for nr_str in lista_stringuri:
        lista_ints.append(int(nr_str))
    return lista_ints

def run():
    lista_numere=[]
    is_running=True
    while is_running:
        afiseaza_meniu()
        option=input("Introduceti optiunea:  ")
        option=option.strip().upper()
        if option=='1':
            #citire lista
            list_string=input("Lista este: ")
            lista_numere=transforma_in_lista(list_string)
            pass
        elif option=='2':
            #add la lista
            pass
        elif option=='E':
            is_running=False
        else:
            print("Nu exista optiunea.")

run()
