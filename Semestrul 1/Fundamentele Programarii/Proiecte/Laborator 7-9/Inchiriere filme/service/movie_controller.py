from domain.validation import MovieValidation
from repository.movie_repository import MovieRepository
from domain.movie import Movie
import random,string

class ControllerMovie:
    def __init__(self,repo:MovieRepository,validator:MovieValidation):
        self.__repo = repo
        self.__validator = validator

    def add_movie(self, id: int, title: str, description: str, type: str):
        """
        Adauga un film in lista de filme
        :param id: id-ul melodiei pe care vrem sa o adaugam
        :param title: titlul melodiei pe care vrem sa o adaugam
        :param description: descrierea melodiei pe care vrem sa o adaugam
        :param type: genul melodiei pe care vrem sa o adaugam
        :return: -;
        :raises: ValueError daca filmul nu este valid
        """
        movie = Movie(id, title, description, type)
        self.__validator.validate(movie)
        self.__repo.store(movie)

    def update_movie(self, id: int, new_title: str, new_description: str, new_type: str):
        """
        Actualizeaza filmul cu id-ul id cu informatiile date
        :param id:id-ul filmului de actualizat
        :param new_title:nout titlu al filmului
        :param new_description:noua descriere a filmului
        :param new_type:noul gen al filmului
        :return:-
        :raises: ValueError daca din informatiile date nu se poate construi un film valid
                ValueError daca nu exista un film cu id dat
        """
        new_movie = Movie(id, new_title, new_description, new_type)
        self.__validator.validate(new_movie)
        self.__repo.update(new_movie)

    def find_movie(self, id: int):
        """
        Cauta filmul cu id-ul dat
        :param id:id-ul de cautat
        :return:filmul cu id-ul dat,daca acesta exista,None altfel
        """
        return self.__repo.find(id)

    def delete_movie(self, id: int):
        """
        Sterge filmul cu id-ul dat
        :param id: id-ul de sters
        :return: -
        :raises: ValueError daca filmul cu id-ul dat nu exista
        """
        self.__repo.delete(id)

    def add_default(self):
        """
        Adauga filme default la lista
        :return: -
        """
        if self.__repo.find(1) is None:
            self.add_movie(1,"Titlu1","Descriere1","Actiune")
        if self.__repo.find(2) is None:
            self.add_movie(2,"Titlu2","Descriere2","Horror")
        if self.__repo.find(3) is None:
            self.add_movie(3,"Titlu3","Descriere3","Drama")
        if  self.__repo.find(4) is None:
            self.add_movie(4,"Titlu4","Descriere4","Actiune")
        if self.__repo.find(5) is None:
            self.add_movie(5,"Titlu5","Descriere5","Comedie")
        if self.__repo.find(6) is None:
            self.add_movie(6,"Titlu6","Descriere6","Horror")
        if self.__repo.find(7) is None:
            self.add_movie(7,"Titlu7","Descriere7","Actiune")

    def add_random(self):
        """
        Adauga filme random la lista
        :return:
        """
        n = random.randint(1, 20)
        characters = string.ascii_letters
        for i in range(n):
            random_id = random.randint(1, 1000)
            m = random.randint(1, 30)
            random_title = ''.join(random.choices(characters, k=m))
            m = random.randint(1, 30)
            random_description = ''.join(random.sample(characters, m))
            random_type = random.choice(['Actiune', 'Comedie', 'Drama', 'Horror', 'Documentar', 'Animat'])
            self.add_movie(random_id, random_title, random_description, random_type)

    def get_all(self):
        """
        Returneaza lista de filme
        :return:
        """
        return self.__repo.get_all()

