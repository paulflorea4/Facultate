def copy_dict(dct:dict):
    """
    Creeaza o copie a dictionarului
    :param dct: dictionarul care urmeaza sa fie copiat
    :return: un nou dictionar care contine noi elemente(dictionare) cu aeleasi valori
    """
    new_dct = {}
    for i,d in dct.items():
        new_d={}
        for key,val in d.items():
            new_list=[]
            for pair in val:
                new_list.append(pair)
            new_d[key]=new_list
        new_dct[i]=new_d
    return new_dct

def convert_to_int(value):
    """
    Converteste valoarea data in numar intreg
    :param value: valoarea
    :return: valoarea intreaga daca se poate converti
    :raises ValueError cu mesajul de eroare daca valoarea nu este numar intreg
    """
    try:
        return int(value)
    except ValueError:
        raise ValueError("Valoarea trebuie sa fie numar natural")

def convert_to_float(value):
    """
    Converteste valoarea data in numar real
    :param value: valoarea
    :return: valoarea reala daca se poate converti
    :raises ValueError cu mesajul de eroare daca valoarea nu este numar real
    """
    try:
        return float(value)
    except ValueError:
        raise ValueError("Valoarea trebuie sa fie numar real")


