from domain.validation import MovieValidation, ClientValidation
from repository.client_repository import ClientFileRepository, ClientRepository
from repository.movie_repository import MovieFileRepository, MovieRepository
from repository.rent_repository import RentFileRepository, RentRepository
from service.client_controller import ControllerClient
from service.movie_controller import ControllerMovie
from service.rent_controller import ControllerRent
from ui.ClientMenu import ClientMenu
from ui.MainConsole import MainConsole
from ui.MovieMenu import MovieMenu
from ui.RentReturnConsole import RentReturnConsole
from colorama import Fore, Style

class MainMenu:
    @staticmethod
    def print_menu():
        print("1.Memorie")
        print("2.Fisier")
        print("E.Iesire din aplicatie")

    def run(self):
        while True:
            self.print_menu()
            user_input = input(">>>").upper().strip()
            match user_input:
                case "1":
                    movie_validator = MovieValidation()
                    movie_repository = MovieRepository()
                    movie_service = ControllerMovie(movie_repository, movie_validator)
                    movie_console = MovieMenu(movie_service)

                    client_validator = ClientValidation()
                    client_repository = ClientRepository()
                    client_service = ControllerClient(client_repository, client_validator)
                    client_console = ClientMenu(client_service)

                    rent_repository = RentRepository()
                    rent_service = ControllerRent(movie_repository, client_repository, rent_repository)
                    rent_console = RentReturnConsole(rent_service)

                    console = MainConsole(client_console, movie_console, rent_console)
                    console.run()

                case "2":
                    movie_validator = MovieValidation()
                    movie_repository = MovieFileRepository("data/movies.txt")
                    movie_service = ControllerMovie(movie_repository, movie_validator)
                    movie_console = MovieMenu(movie_service)

                    client_validator = ClientValidation()
                    client_repository = ClientFileRepository("data/clients.txt")
                    client_service = ControllerClient(client_repository, client_validator)
                    client_console = ClientMenu(client_service)

                    rent_repository = RentFileRepository("data/rents.txt")
                    rent_service = ControllerRent(movie_repository, client_repository, rent_repository)
                    rent_console = RentReturnConsole(rent_service)

                    console = MainConsole(client_console, movie_console, rent_console)
                    console.run()

                case "E":
                    print("Exiting...")
                    break
                case _:
                    print(Fore.RED+"Optiune invalida"+Style.RESET_ALL)