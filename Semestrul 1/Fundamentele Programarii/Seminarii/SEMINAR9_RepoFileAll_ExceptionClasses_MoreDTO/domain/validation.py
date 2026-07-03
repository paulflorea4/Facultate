import math

from domain.melodie import Melodie
from exceptions.exceptions import ValidationException


class ValidatorMelodie:
    def validate(self, melodie: Melodie):
        """
         Valideaza o melodie data
         :param melodie: melodia de validat
         :return: -
         :raises: ValidationException cu mesajele de eroare daca melodia nu este valida
         """
        errors = []
        if len(melodie.get_titlu()) < 2:
            errors.append("Titlul melodiei trebuie sa aiba cel putin un caracter.")
        if len(melodie.get_artist()) < 1:
            errors.append("Artistul melodiei trebuie sa aiba cel putin un caracter.")

        if melodie.get_gen() not in ['rock', 'pop', 'hip-hop', 'folk']:
            errors.append("Genul melodiei poate fi doar rock, pop, hip-hop sau folk.")

        # de adaugat functii get_minute, get_secunde pentru a nu face calculele aici
        durata_melodie = melodie.get_durata()
        minute = int(durata_melodie)
        secunde = int((durata_melodie - minute) * 100)

        if minute < 1 or minute > 15:
            errors.append("Melodia ar trebui sa aiba intre 1 si 15 minute.")
        if not 0 <= secunde <= 59:
            errors.append("Numarul de secunde trebuie sa fie intre 1 si 59.")

        if len(errors) > 0:
            error_message = '\n'.join(errors)
            raise ValidationException(error_message)


class ValidatorPersoana:
    def validate(self, persoana):
        errors = []

        if len(persoana.cnp) != 13:
            errors.append("CNP trebuie sa fie format din 13 cifre.")

        if len(persoana.nume.split()) < 2:
            errors.append("Persoana trebuie sa aiba nume si prenume (i.e. cel putin 2 cuvinte).")

        if len(errors) > 0:
            error_str = '\n'.join(errors)
            raise ValidationException(error_str)


class ValidatorRating:
    def validate(self, rating):
        errors = []

        if not 1 <= rating.scor <= 5:
            errors.append("Scorul evaluarii trebuie sa fie intre 1 si 5.")

        if len(errors) > 0:
            error_str = '\n'.join(errors)
            raise ValidationException(error_str)
