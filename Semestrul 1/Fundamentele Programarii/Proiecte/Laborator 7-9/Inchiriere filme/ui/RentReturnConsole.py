import math
from domain.movie import Movie
from domain.rent import Rent
from domain.client import Client
from colorama import Fore, Style

class RentReturnConsole:
    def __init__(self,rent_service):
        self.__serv=rent_service

    @staticmethod
    def print_menu():
        print("1.Inchiriere film")
        print("2.Returnare film")
        print("3.Cele mai inchiriate filme")
        print("4.Clienti cu filme inchiriate")
        print("5.Cele mai putin inchiriate n filme")
        print("P.Afisare lista de inchirieri")
        print("D.Default")
        print("E.Inapoi")

    def add_rent_ui(self):
        try:
            client_id = int(input("Id-ul clientului:"))
            movie_id = int(input("Id-ul filmului:"))
        except ValueError:
            print(Fore.RED+"Id invalid"+Style.RESET_ALL)
            return
        try:
            self.__serv.add_rent(client_id,movie_id)
            print(Fore.GREEN+"Film inchiriat cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED+str(e)+Style.RESET_ALL)

    def delete_rent_ui(self):
        try:
            client_id = int(input("Id-ul clientului:"))
            client=self.__serv.find_client(client_id)
            if client is None:
                print(Fore.RED+f"Clientul cu id-ul [{client_id}] nu exista"+Style.RESET_ALL)
                return
            movie_id = int(input("Id-ul filmului:"))
            movie=self.__serv.find_movie(movie_id)
            if movie is None:
                print(Fore.RED+f"Filmul cu id-ul [{movie_id}] nu exista"+Style.RESET_ALL)
                return
        except ValueError:
            print(Fore.RED+"Id invalid"+Style.RESET_ALL)
            return
        try:
            rent=Rent(client,movie)
            self.__serv.delete_rent(rent)
            print(Fore.GREEN+"Film returnat cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED+str(e)+Style.RESET_ALL)

    def most_rented_movies_ui(self):
        rents=self.__serv.rented_movies()
        for key,value in rents:
            print(Movie(key,value['movie_title'],value['movie_description'],value['movie_type']),end=", Inchirieri = ")
            print(value['movie_rents'])

    def least_rented_n_movies_ui(self):
        n=input("Numarul maxim de inchirieri=")
        try:
            n=int(n)
        except ValueError:
            print(Fore.RED+"Numar invalid"+Style.RESET_ALL)

        rents=self.__serv.rented_movies(n)
        for rent in rents:
            print(rent[1]['movie_title']+':'+str(rent[1]['movie_rents']))

    def clients_with_rented_movies_ui(self):
        print("1.Ordonare dupa nume")
        print("2.Ordonare dupa numarul de filme inchiriate")
        print("3.Primi 30% cu cele mai multe filme")
        print("E.Inapoi")

        user_input=input(">>>").strip().upper()
        match user_input:
            case "1":
                sorted_by_name=self.__serv.clients_with_rented_movies(user_input)
                for key, value in sorted_by_name:
                    print(Client(key, value['client_name'], value['client_cnp']), end=", Inchirieri = ")
                    print(value['client_rents'])
            case "2":
                sorted_by_rent=self.__serv.clients_with_rented_movies(user_input)
                for key, value in sorted_by_rent:
                    print(Client(key, value['client_name'], value['client_cnp']), end=", Inchirieri = ")
                    print(value['client_rents'])
            case "3":
                sort_by_rent=self.__serv.clients_with_rented_movies(user_input)
                for index,value in enumerate(sort_by_rent):
                    print(value[1]['client_name']+':'+str(value[1]['client_rents']))
            case "E":
                pass
            case _:
                print(Fore.RED+"Optiune invalida"+Style.RESET_ALL)

    def print_rents_ui(self,rents):
        for rent in rents:
            print(rent)

    def run(self):
        is_running=True
        while is_running:
            self.print_menu()
            user_input=input(">>>").upper().strip()
            match user_input:
                case "1":
                    self.add_rent_ui()
                case "2":
                    self.delete_rent_ui()
                case "3":
                    self.most_rented_movies_ui()
                case "4":
                    self.clients_with_rented_movies_ui()
                case "5":
                    self.least_rented_n_movies_ui()
                case "P":
                    self.print_rents_ui(self.__serv.get_all())
                case "D":
                        try:
                            self.__serv.add_default()
                            print(Fore.GREEN+"S-au adaugat inchirierile default"+Style.RESET_ALL)
                        except ValueError:
                            pass
                case "E":
                    is_running=False
                case _:
                    print(Fore.RED+"Optiune invalida"+Style.RESET_ALL)