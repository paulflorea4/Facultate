from domain.client import Client
from domain.movie import Movie

class ClientValidation:
    def validate(self,client:Client):
        """
        Valideaza datele unui client dat
        :param client: datele clientului de validat
        :return: -
        :raises:ValueError cu mesajele de eroare daca datele clientului nu sunt valide
        """
        errors=[]
        if len(client.name)<1:
            errors.append("Numele clientului trebuie sa aiba cel putin un caracter")
        if not all(char.isdigit() for char in client.cnp):
            errors.append("CNP-ul contine doar cifre")
        if len(client.cnp)<3:
            errors.append("CNP-ul trebuie sa fie de minim 3 cifre")
        if not client.name.isalpha():
            errors.append("Numele clientului contine doar litere")

        if len(errors)>0:
            error_message='\n'.join(errors)
            raise ValueError(error_message)

class MovieValidation:
    def validate(self, movie:Movie):
        """
        Valideaza datele unui film dat
        :param movie: datele filmului de validat
        :return: -
        :raises: ValueError cu mesajele de eroare daca datele filmului nu sunt valide
        """
        errors=[]
        if len(movie.title) < 1:
            errors.append('Titlul filmului trebuie sa aiba cel putin un caracter')
        if len(movie.description) < 1:
            errors.append('Descrierea filmului trebuie sa aiba cel putin un caracter')
        if movie.type not in ['Actiune','Comedie','Drama','Horror','Documentar','Animat']:
            errors.append('Genul filmului poate fi doar actiune,comedie,drama,horror,documentar sau animat')

        if len(errors)>0:
            error_message = '\n'.join(errors)
            raise ValueError(error_message)