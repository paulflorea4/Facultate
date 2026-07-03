class MainConsole:
    def __init__(self,client_menu,movie_menu,rent_return_menu):
        self.__client_menu = client_menu
        self.__movie_menu = movie_menu
        self.__rent_return_menu=rent_return_menu

    @staticmethod
    def print_menu():
        print("1.Clienti")
        print("2.Filme")
        print("3.Inchiriere/returnare")
        print("E.Inapoi")

    def run(self):
        is_running = True
        while is_running:
            self.print_menu()
            user_input = input(">>>").upper().strip()
            match user_input:
                case "1":
                    self.__client_menu.run()
                case "2":
                    self.__movie_menu.run()
                case "3":
                    self.__rent_return_menu.run()
                case "E":
                    is_running = False
                case _:
                    print("Optiune invalida")
