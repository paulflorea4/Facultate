"""
tip:tipul cheltuielii(str,poate fi una dintre apa,canal,incalzire,gaz,altele)
suma:suma cheltuielii(numar real pozitiv)
index:numarul apartamentului(numar natural)
"""

def validate_cost(cost_type,cost_value):
    """
    Valideaza cheltuiala unui apartament dat
    :param cost_type: Tipul cheltuielii
    :param cost_value: Suma cheltuielii
    :return: -
    :raises: ValueError cu mesajele de eroare daca cheltuiala nu este valida
    """
    errors = []
    if cost_type not in ['apa','canal','incalzire','gaz','internet']:
        errors.append("Tipul cheltuielii poate fi doar apa,canal,incalzire,gaz,internet")
    if cost_value <=0 :
        errors.append("Suma cheltuielii trebe sa fie un numar real strict pozitiv")
    if len(errors) >0:
        error_massage='\n'.join(errors)
        raise ValueError(error_massage)



