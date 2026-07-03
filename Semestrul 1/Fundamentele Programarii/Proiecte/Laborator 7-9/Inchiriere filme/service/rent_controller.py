import math

from domain.rent import Rent
from utils.utilities import mergeSort, bingoSort


class ControllerRent:
    def __init__(self,movie_repo,client_repo,rent_repo):
        self.__movie_repo = movie_repo
        self.__client_repo = client_repo
        self.__rent_repo = rent_repo

    def add_rent(self,client_id,movie_id):
        """
        Adauga o inchiriere
        :param client_id:id-ul clientutului care face inchirierea
        :param movie_id:id-ul filmului pentru care se face inchirierea
        :return:-
        :raises:ValueError daca nu exista clientul cu id-ul dat
                ValueError daca nu exista filmul cu id-ul dat
                ValueError daca mai exista o inchiriere pentru filmul si persoana data
        """
        client=self.__client_repo.find(client_id)
        if client is None:
            raise ValueError(f"Nu exista client cu id-ul [{client_id}]")
        movie=self.__movie_repo.find(movie_id)
        if movie is None:
            raise ValueError(f"Nu exista film cu id-ul [{movie_id}]")
        rent=Rent(client,movie)
        self.__rent_repo.store(rent)

    def rented_movies(self,k=None):
        """
        Returneaza informatii despre cele mai inchiriate filme
        :return:dictionar cu cheie=id film si valoare=un alt dictionar in care se pastreaza titlu filmului,genul filmului,descrierea si numarul de inchiriei
        """
        rents_dict={}

        all_rents=self.__rent_repo.get_all()
        for rent in all_rents:
            if rent.movie.get_id() not in rents_dict:
                rents_dict[rent.movie.get_id()]={'movie_title':rent.movie.title,
                                                 'movie_type':rent.movie.type,
                                                 'movie_description':rent.movie.description,
                                                 'movie_rents':1
                }
            else:
                rents_dict[rent.movie.get_id()]['movie_rents']+=1

        if k is None:
            sorted_rents=list(rents_dict.items())
            bingoSort(sorted_rents,len(sorted_rents),lambda x: x[1]['movie_rents'],True)
            return sorted_rents
        rents_deep_copy=rents_dict.copy()
        for rent in rents_deep_copy:
            if rents_dict[rent]['movie_rents']>=k:
                del rents_dict[rent]
        sorted_rents = list(rents_dict.items())
        bingoSort(sorted_rents,len(sorted_rents),lambda x: x[1]['movie_rents'],False)
        return sorted_rents


    def clients_with_rented_movies(self,k=None):
        """
        Returneaza informatii despre clientii cu filme inchiriate
        :return:dictionar cu cheie=id si valoare alt dictionar in care se pastreaza nume,cnp si numarul de inchiriei
        """
        clients_with_rents={}
        all_rents=self.__rent_repo.get_all()
        for rent in all_rents:
            if rent.client.get_id() not in clients_with_rents:
                clients_with_rents[rent.client.get_id()]={'client_name':rent.client.name,
                                                          'client_cnp':rent.client.cnp,
                                                          'client_rents':1
                }
            else:
                clients_with_rents[rent.client.get_id()]['client_rents']+=1

        if k is None:
            return clients_with_rents
        sorted_clients = list(clients_with_rents.items())
        if k=='1':
            mergeSort(sorted_clients,0,len(clients_with_rents),lambda x: x[1]['client_name'],False)
            return sorted_clients
        if k=='2':
            mergeSort(sorted_clients, 0, len(clients_with_rents), lambda x: x[1]['client_rents'], False)
            return sorted_clients
        if k=='3':
            mergeSort(sorted_clients, 0, len(clients_with_rents), lambda x: x[1]['client_rents'], True)
            n = math.floor(0.3 * self.get_len())
            first_n_sorted_clients=sorted_clients[:n]
            return first_n_sorted_clients

    def add_default(self):
        """
        Adauga inchirieri default la lista
        :return: -
        """
        self.add_rent(1,3)
        self.add_rent(2,4)
        self.add_rent(3,4)
        self.add_rent(4,2)
        self.add_rent(4,3)
        self.add_rent(1,4)
        self.add_rent(5,3)
        self.add_rent(2,1)
        self.add_rent(3,2)
        self.add_rent(2,5)
        self.add_rent(3,3)

    def delete_rent(self,rent:Rent):
        """
        Sterge inchirierea data
        :param rent: inchirierea de sters
        :return: -
        :raises: ValueError daca inchirierea data nu exista
        """
        self.__rent_repo.delete(rent)

    def find_client(self,id):
        """
        Cauta clientul cu id-ul dat
        :param id: id-ul de cautat
        :return: clientul cu id-ul dat,daca acesta exista,None altfel
        """
        return self.__client_repo.find(id)

    def find_movie(self,id):
        """
        Cauta filmul cu id-ul dat
        :param id:id-ul de cautat
        :return:filmul cu id-ul dat,daca acesta exista,None altfel
        """
        return self.__movie_repo.find(id)

    def get_all(self):
        return self.__rent_repo.get_all()

    def get_len(self):
        return len(self.__rent_repo.get_all())
