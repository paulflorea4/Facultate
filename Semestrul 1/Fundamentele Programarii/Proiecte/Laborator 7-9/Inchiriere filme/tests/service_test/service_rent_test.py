import unittest
from repository.movie_repository import MovieRepository
from repository.client_repository import ClientRepository
from repository.rent_repository import RentRepository
from service.rent_controller import ControllerRent
from domain.movie import Movie
from domain.client import Client
from domain.rent import Rent


def add_default(movie_repository: MovieRepository, client_repository: ClientRepository,
                rent_repository: RentRepository):
    movie1 = Movie(1, 'Titlu1', 'Descriere1', 'Comedie')
    movie2 = Movie(2, 'Titlu2', 'Descriere2', 'Actiune')
    movie3 = Movie(3, 'Titlu3', 'Descriere3', 'Actiune')
    movie4 = Movie(4, 'Titlu4', 'Descriere4', 'Drama')
    movie5 = Movie(5, 'Titlu5', 'Descriere5', 'Horror')
    movie6 = Movie(6, 'Titlu6', 'Descriere6', 'Documentar')
    movie7 = Movie(7, 'Titlu7', 'Descriere7', 'Comedie')
    movie8 = Movie(8, 'Titlu8', 'Descriere8', 'Horror')

    movie_repository.store(movie1)
    movie_repository.store(movie2)
    movie_repository.store(movie3)
    movie_repository.store(movie4)
    movie_repository.store(movie5)
    movie_repository.store(movie6)
    movie_repository.store(movie7)
    movie_repository.store(movie8)

    client1 = Client(1, "Cristian", "321321321")
    client2 = Client(2, "Alex", "32546521")
    client3 = Client(3, "Tudor", "321432321")
    client4 = Client(4, "Florin", "3211321321")
    client5 = Client(5, "Adrian", "32135321")

    client_repository.store(client1)
    client_repository.store(client2)
    client_repository.store(client3)
    client_repository.store(client4)
    client_repository.store(client5)

    rent_repository.store(Rent(client1, movie2))
    rent_repository.store(Rent(client2, movie2))
    rent_repository.store(Rent(client2, movie3))
    rent_repository.store(Rent(client3, movie5))
    rent_repository.store(Rent(client1, movie6))
    rent_repository.store(Rent(client1, movie7))
    rent_repository.store(Rent(client4, movie1))
    rent_repository.store(Rent(client2, movie4))
    rent_repository.store(Rent(client2, movie8))
    rent_repository.store(Rent(client5, movie6))
    rent_repository.store(Rent(client3, movie8))
    rent_repository.store(Rent(client4, movie7))
    rent_repository.store(Rent(client4, movie8))
    rent_repository.store(Rent(client5, movie5))
    rent_repository.store(Rent(client4, movie3))


class TestControllerRent(unittest.TestCase):
    def setUp(self):
        self.movie_repository = MovieRepository()
        self.client_repository = ClientRepository()
        self.rent_repository = RentRepository()
        add_default(self.movie_repository, self.client_repository, self.rent_repository)
        self.test_service = ControllerRent(self.movie_repository, self.client_repository, self.rent_repository)

    def test_add_rent(self):
        self.assertEqual(self.test_service.get_len(), 15)

        self.test_service.add_rent(1, 3)
        self.assertEqual(self.test_service.get_len(), 16)

        self.assertRaises(ValueError, self.test_service.add_rent, 1, 9)

        self.assertRaises(ValueError, self.test_service.add_rent,1, 2)

    def test_delete_rent(self):
        self.test_service.delete_rent(Rent(self.client_repository.find(1), self.movie_repository.find(2)))
        self.assertEqual(self.test_service.get_len(), 14)

        self.assertRaises(ValueError, self.test_service.delete_rent, Rent(self.client_repository.find(1), self.movie_repository.find(2)))

        self.assertRaises(ValueError,self.test_service.delete_rent,Rent(self.client_repository.find(1), self.movie_repository.find(9)))


    def test_most_rented_movies(self):
        sorted_list = self.test_service.rented_movies()

        self.assertEqual(sorted_list[0][1]['movie_rents'], 3)
        self.assertEqual(sorted_list[1][1]['movie_rents'], 2)

    def test_clients_with_rented_movies(self):
        clients_list = self.test_service.clients_with_rented_movies()

        self.assertEqual(clients_list[1]['client_rents'], 3)
        self.assertEqual(clients_list[2]['client_rents'], 4)
        self.assertEqual(clients_list[3]['client_rents'], 2)
        self.assertEqual(clients_list[4]['client_rents'], 4)
        self.assertEqual(clients_list[5]['client_rents'], 2)


if __name__ == "__main__":
    unittest.main()