"""
titlu: titlul melodiei (str, cel putin 2 caractere)
artist: artistul melodiei (str, cel putin 1 caracter)
gen: genul melodiei (str, poate fi doar unul dintre: rock, pop, hip-hop, folk)
durata: durata melodiei, sub forma mm.ss (float; numărul de minute
trebuie să fie între 1 și 15, iar numărul de secunde între 1 și 59)
"""
from domain.melodie import get_titlu, get_artist, get_gen, get_durata


def valideaza_melodie(melodie):
    """
    Valideaza o melodie data
    :param melodie: melodia de validat
    :return: -
    :raises: ValueError cu mesajele de eroare daca melodia nu este valida
    """
    errors = []
    if len(get_titlu(melodie)) < 2:
        errors.append("Titlul melodiei trebuie sa aiba cel putin un caracter.")
    if len(get_artist(melodie)) < 1:
        errors.append("Artistul melodiei trebuie sa aiba cel putin un caracter.")

    if get_gen(melodie) not in ['rock', 'pop', 'hip-hop', 'folk']:
        errors.append("Genul melodiei poate fi doar rock, pop, hip-hop sau folk.")

    #de adaugat functii get_minute, get_secunde pentru a nu face calculele aici
    durata_melodie = get_durata(melodie)
    minute = int(durata_melodie)
    secunde = int((durata_melodie - minute) * 100)

    if minute < 1 or minute > 15:
        errors.append("Melodia ar trebui sa aiba intre 1 si 15 minute.")
    if not 1 < secunde < 59:
        errors.append("Numarul de secunde trebuie sa fie intre 1 si 59.")

    if len(errors) > 0:
        error_message = '\n'.join(errors)
        raise ValueError(error_message)
