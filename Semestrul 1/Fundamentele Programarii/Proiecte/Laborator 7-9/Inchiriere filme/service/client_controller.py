from domain.validation import ClientValidation
from repository.client_repository import ClientRepository
from domain.client import Client

class ControllerClient:
    def __init__(self,repo:ClientRepository,validator:ClientValidation):
        self.__repo = repo
        self.__validator = validator

    def add_client(self, id: int, name: str, cnp: str):
        """
        Adauga un client in lista de clienti
        :param id: id-ul clientului pe care vrem sa il adaugam
        :param name: numele clientului pe care vrem sa il adaugam
        :param cnp: CNP-ul clientului pe care vrem sa il adaugam
        :return: -;
        :raises: ValueError daca datele clentului nu sunt valide
        """
        client = Client(id, name, cnp)
        self.__validator.validate(client)
        self.__repo.store(client)

    def update_client(self,id:int, new_name:str, new_cnp:str):
        """
        Actualizeaza clientul cu id-ul id cu informatiile date
        :param id:id-ul clientului de actualizat
        :param new_name:noul nume al clientului
        :param new_cnp:noul CNP al clientului
        :return:-
        :raises:ValueError daca din informatiile date nu se poate construi un client vallid
                ValueError daca nu exista un client cu id dat
        """
        new_client=Client(id,new_name,new_cnp)
        self.__validator.validate(new_client)
        self.__repo.update(new_client)

    def find_client(self,id:int):
        """
        Cauta clientul cu id-ul dat
        :param id: id-ul de cautat
        :return: clientul cu id-ul dat,daca acesta exista,None altfel
        """
        return self.__repo.find(id)

    def delete_client(self,id:int):
        """
        Sterge clientul cu id-ul dat
        :param id: id-ul de sters
        :return:-
        :raises:ValueError daca clientul cu id-ul dat nu exista
        """
        self.__repo.delete(id)

    def add_default(self):
        """
        Adauga clienti default la lista
        :return: -
        """
        if self.__repo.find(1) is None:
            self.add_client(1, "Andrei", "123")
        if self.__repo.find(2) is None:
            self.add_client(2, "Mihai", "423")
        if self.__repo.find(3) is None:
            self.add_client(3, "Cristian", "431")
        if self.__repo.find(4) is None:
            self.add_client(4, "Tudor", "543")
        if self.__repo.find(5) is None:
            self.add_client(5, "Alexandru", "873")

    def get_all(self):
        """
        Returneaza lista de clienti
        :return:
        """
        return self.__repo.get_all()


