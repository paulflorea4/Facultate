from domain.persoana import Persoana
from exceptions.exceptions import PersonAlreadyExistsException, PersonDoesNotExistException, RepositoryException


# Repo care lucreaza cu date din fisier (spre exemplu, fisierul persons_default.txt)
class PersonFileRepository:
    def __init__(self, filename):
        self.__filename = filename

    def store(self, person):
        """
        Adauga o persoana la colectia de persoane
        :param person: persoana de adaugat
        :return: -; colectia de persoane se modifica prin adaugarea persoanei date
        :raises: ValueError daca se incearca adaugarea unei persoane cu CNP care exista deja
        """
        if self.find(person.cnp) is not None:
            raise PersonAlreadyExistsException()

        persons = self.__load_from_file()
        persons.append(person)
        self.__save_to_file(persons)

        # line_to_write = person.cnp + ',' + person.nume + "\n"
        # with open(self.__filename, 'a') as file:
        #     file.write(line_to_write)

    def find(self, cnp):
        """
        Cauta persoana cu cnp dat
        :param id: cnp-ul cautat
        :return: obiect de tip Persoana daca exista persoana cu cnp dat, None altfel
        """
        persons = self.__load_from_file()
        for existing_person in persons:
            if existing_person.cnp == cnp:
                return existing_person
        return None

    def update(self, person):
        pass

    def delete(self, cnp):
        """
        Sterge persoana cu CNP dat
        :param cnp: cnp-ul dupa care se sterge
        :return: persoana care s-a sters; lista este modificata prin stergerea persoanei cu cnp dat,
                 daca aceasta exista
        :raises: ValueError daca nu exista persoana cu CNP dat
        """
        person_to_delete = self.find(cnp)
        if person_to_delete is None:
            raise PersonDoesNotExistException()

        persons = self.__load_from_file()
        persons.remove(person_to_delete)
        self.__save_to_file(persons)

        return person_to_delete

    def __load_from_file(self):
        """
        Incarca datele din fisier
        :return: o lista cu obiectele de tip Persoana construite pe baza informatiilor din fisier
        :raises: RepositoryException daca exista probleme la scrierea in fisier
        """
        try:
            persons = []
            with open(self.__filename, "r") as file:
                lines = file.readlines()
                for line in lines:
                    line = line.strip()
                    if line:
                        cnp, nume = line.split(",")
                        p = Persoana(cnp.strip(), nume.strip())
                        persons.append(p)
                return persons
        except IOError:
            raise RepositoryException("Nu s-a putut citi din fisierul: " + self.__filename)

    def __save_to_file(self, persoane):
        """
        Salveaza datele despre persoanele din lista data in fisier
        :param persoane: lista cu persoane
        :raises: RepositoryException daca exista probleme la scrierea in fisier
        """
        try:
            with open(self.__filename, "w") as file:
                for person in persoane:
                    person_line = person.cnp + "," + person.nume + "\n"
                    file.write(person_line)
        except IOError:
            raise RepositoryException("Nu s-au putut salva datele in fisierul: " + self.__filename)

    def get_all(self):
        return self.__load_from_file()

    def size(self):
        return len(self.__load_from_file())
