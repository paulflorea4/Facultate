
def print_menu():
    """
    afiseaza meniul utilizatorului
    input:-
    output:-
    """
    print("1.Citire lista")
    print("2.Afisare lista")
    print("3.Cea mai lunga secventa strict crescatoare din lista")
    print("4.Cea mai lunga secventa cu toate elementele egale")
    print("5.Cea mai lunga secventa cu oricare 2 elemente consecutive care difere print-un numar prim")
    print("6.Iesire")

def prim(x):
    """
    verifica daca numarul dat e prim
    input:x-numar intreg
    output:True-daca x e prim,False-in caz contrar
    """
    div=0;i=1
    while i<=x:
        if x%i==0:
            div=div+1
        i=i+1
    if div==2:
        return True
    return False

def transform_to_int_list(s_list):
    """
    separa termenii intregi dintr-un string,adaugandu-i intr-o lista
    input:s_list-lista de numere sub forma de string
    output:lista termenilor intregi
    """
    split_list=s_list.split(",")
    numbers_list=[]
    for nr_str in split_list:
        numbers_list.append(int(nr_str))
    return numbers_list

def property_option(option,number1,number2):
    """
    verifica proprietatea a 2 numere in functie de optiunea aleasa
    input:option-optiunea selectata de utilizator(numar natural din multimea {3,4}),number1,number2-2 numere intregi
    output:True-daca primul numar este strict mai mic decat al doilea(optiunea 3),False-in caz contrar
            True-daca numerele sunt egale(optiunea 4),False-in caz contrar
            True-daca numere difera print-un numar prim(optiunea 5),False-in caz contrar
    """
    if option=='3':
        return number1<number2
    if option=='4':
        return number1==number2
    if option=='5':
        return prim(abs(number1-number2))

def longest_streak_length(list,option):
    """
    returneaza lungimea maxima a secventei cu proprietatea ceruta din lista data
    input:list-lista de numere intregi,option-optiunea selectata de utilizator(numar natural din multimea {3,4})
    output:lmax-lungimea maxima gasita
    """
    l=1;lmax=1
    for i in range(len(list)-1):
        if  property_option(option,list[i],list[i+1]):
            l=l+1
        else:
            if l>lmax:
                lmax=l
            l=1
    if l>lmax:
        lmax=l
    return lmax

def longest_streaks(list,option):
    """
    returneaza listele de lungime maxima cu proprietatea ceruta
    input:list-lista de numere intregi,option-optiunea selectata de utilizator(numar natural din multimea {3,4})
    output:multiple_lists-lista de liste de lungime maxima cu proprietatea ceruta
    """
    multiple_lists=[];lmax=longest_streak_length(list,option);current_list=[list[0]]
    for i in range(len(list)-1):
        if property_option(option,list[i],list[i+1]):
            current_list.append(list[i+1])
        else:
            if len(current_list)==lmax:
                multiple_lists.append(current_list)
            current_list=[list[i+1]]
    if len(current_list)==lmax:
        multiple_lists.append(current_list)
    return multiple_lists

def test():
    """
    ruleaza toate testele din progream
    input:-
    output:-
    """
    assert longest_streak_length([1,2,3,4,1,1,1,1,2,3,4,5],'3')==5
    assert longest_streak_length([1, 2, 3, 4, 5, 1, 1, 1, 1, 2, 3, 4, 5], '3') == 5
    assert longest_streak_length([1, 2, 3, 4, 6, 1, 1, 1, 1, 2, 3, 4, 5], '3') == 5
    assert longest_streak_length([1, 2, 3, 4, 5, 1, 1, 1, 1, 2, 3, 4, 5], '4') == 4
    assert longest_streak_length([1, 1, 3, 4, 5, 1, 1, 1, 1, 1, 1, 4, 5], '4') == 6
    assert longest_streak_length([1,2,3,4,5,6],'4')==1
    assert longest_streak_length([1,2,3,4,1,1,3,5,7,9,3,5,7],'5')==5

    assert longest_streaks([1, 2, 3, 4, 6, 1, 1, 1, 1, 2, 3, 4, 5], '3') == [[1,2,3,4,6],[1,2,3,4,5]]
    assert longest_streaks([1, 2, 3, 4, 6, 1, 1, 1, 1, 2, 3, 4, 5], '4') == [[1,1,1,1]]
    assert longest_streaks([1, 2, 3, 4, 6, 1, 1, 1, 1, 2, 3, 4, 5, 2, 2, 2, 2], '4') == [[1, 1, 1, 1],[2,2,2,2]]
    assert longest_streaks([1,2,3,4,1,1,3,5,7,9,3,5,7],'5')==[[1,3,5,7,9]]

def run():
    """
    porneste interfata utilizator tip consola cu un meniu
    input:-
    output:-
    """
    numbers_list=[]
    while True:
        print_menu() #apeleaza functia pentru afisarea meniului
        option=input("Introduceti optiunea:")
        option=option.strip()
        try:
            if option=="1":
                #citirea numerelor
                list_as_str=input("Introduceti o lista de numere,separate prin virgula: ")
                if(len(list_as_str))==0:
                    print("Invalid",end='\n')
                    continue
                print()
                numbers_list=transform_to_int_list(list_as_str)
            elif option=="2":
                #afisarea listei de numere
                print("Lista curenta:",numbers_list,end='\n')
            elif option=="3" or option=="4" or option=="5":
                #gaseste secventa cu proprietatea ceruta
                multiple_list=longest_streaks(numbers_list,option)
                #afiseaza secventele
                for list in multiple_list:
                    print(list)
                print()
            elif option=="6":
                #iese din programul principal
                print("Iesire")
                break
            else:
                #afiseaza mesajul in cazul in care optiunea nu este in meniu
                print(f"Optiunea '{option}' este invalida",end='\n\n')

        except Exception:
            print("Invalid")
            print()


test()
run()

