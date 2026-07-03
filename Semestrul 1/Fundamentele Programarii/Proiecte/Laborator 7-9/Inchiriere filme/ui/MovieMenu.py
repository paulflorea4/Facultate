from colorama import Fore,Style

class MovieMenu:
    def __init__(self,movie_service):
        self.__serv=movie_service

    @staticmethod
    def print_menu():
        print("1.Adauga film")
        print("2.Cauta film")
        print("3.Sterge film")
        print("4.Actualizare film")
        print("P.Afisare lista de filme")
        print("D.Default")
        print("R.Random")
        print("E.Inapoi")

    def movie_info_input(self):
        id=input("Introduceti id-ul filmului:").strip()
        title=input("Introduceti titlul filmului:").strip()
        description=input("Introduceti descrierea filmului:").strip()
        type=input("Introduceti genul filmului:").strip()
        return id,title,description,type

    def add_movie_ui(self):
        id,title,description,type=self.movie_info_input()
        try:
            id=int(id)
        except ValueError:
            print(Fore.RED+"Id-ul este invalid"+Style.RESET_ALL)
            return
        try:
            self.__serv.add_movie(id,title,description,type)
            print(Fore.GREEN+"Film adaugat cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED+str(e)+Style.RESET_ALL)

    def search_movie_ui(self):
        id=input("Introduceti id-ul filmului pe care doriti sa il gasiti:").strip()
        try:
            id=int(id)
            searched_movie = self.__serv.find_movie(id)
            if searched_movie is not None:
                print(searched_movie)
            else:
                print(Fore.RED+f"Filmul cu id-ul [{id}] nu exista"+Style.RESET_ALL)
        except ValueError:
            print(Fore.RED+"Id-ul este invalid"+Style.RESET_ALL)

    def delete_movie_ui(self):
        id=input("Introduceti id-ul filmului pe care doriti sa il stergeti:").strip()
        try:
            id=int(id)
        except ValueError:
            print(Fore.RED+"Id-ul este invalid"+Style.RESET_ALL)
        try:
            self.__serv.delete_movie(id)
            print(Fore.GREEN+"Film sters cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED+str(e)+Style.RESET_ALL)

    def update_movie_ui(self):
        id,new_title,new_description,new_type=self.movie_info_input()
        try:
            self.__serv.update_movie(id,new_title,new_description,new_type)
            print(Fore.GREEN+"Film actualizat cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED+str(e)+Style.RESET_ALL)

    def print_movies_ui(self,movies):
        for movie in movies:
            print(movie)

    def run(self):
        is_running=True
        while is_running:
            self.print_menu()
            user_input=input(">>>").upper().strip()
            match user_input:
                case "1":
                    self.add_movie_ui()
                case "2":
                    self.search_movie_ui()
                case "3":
                    self.delete_movie_ui()
                case "4":
                    self.update_movie_ui()
                case "P":
                    self.print_movies_ui(self.__serv.get_all())
                case "D":
                    try:
                        self.__serv.add_default()
                        print(Fore.GREEN+"S-au adaugat filme default"+Style.RESET_ALL)
                    except ValueError as e:
                        print(Fore.RED+str(e)+Style.RESET_ALL)
                case "R":
                    try:
                        self.__serv.add_random()
                        print(Fore.GREEN+"S-au adaugat filme random"+Style.RESET_ALL)
                    except ValueError as e:
                        print(Fore.RED+str(e)+Style.RESET_ALL)
                case "E":
                    is_running=False
                case _:
                    print(Fore.RED+"Optiune invalida"+Style.RESET_ALL)
