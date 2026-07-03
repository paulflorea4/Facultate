from domain.persoana import Persoana
from domain.validation import ValidatorMelodie, ValidatorPersoana
from repository.repo_persoane import PersonFileRepository


class ServicePersoane:
    def __init__(self, repo, validator: ValidatorPersoana):
        self.__repo = repo
        self.__validator = validator

    def adauga_persoana(self, cnp, nume):
        """
        Adauga persoana cu cnp si nume dat
        :param cnp: cnp-ul persoanei de adaugat
        :param nume: numele persoanei de adaugat
        :return: -; persoana se adauga in colectia de persoane
        :raises: ValueError daca datele despre persoana sunt considerate invalide
                 ValueError daca mai exista o persoana cu CNP dat in colectie
        """
        m = Persoana(cnp, nume)
        self.__validator.validate(m)
        self.__repo.store(m)


    def find_persoana(self, cnp: str):
        """
        Cauta persoana cu cnp dat
        :param id: cnp-ul dupa care se cauta
        :return: persoana cu id-ul dat, daca aceasta exista, None altfel
        """
        return self.__repo.find(cnp)

    def delete_persoana(self, cnp):
        return self.__repo.delete(cnp)

    def get_all(self) -> list:
        """
        Returneaza colectia de persoane
        :return:
        """
        return self.__repo.get_all()
