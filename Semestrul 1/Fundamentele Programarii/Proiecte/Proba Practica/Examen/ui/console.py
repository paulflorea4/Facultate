class Console:
    def __init__(self,service):
        self.__serv=service

    @staticmethod
    def print_menu():
        print("1.Adauga spectacol")
        print("2.Modifica spectacol")
        print("3.Adauga spectacole generate")
        print("4.Exporta spectacole sortate")
        print("E.Exit")

    def __adauga_spectacol_ui(self):
        titlu=input("Introduceti titlu:")
        artist=input("Introduceti artist:")
        gen=input("Introduceti genul:")
        durata=input("Introduceti durata:")
        try:
            durata=int(durata)
        except ValueError:
            print("Durata trebuie sa fie numar intreg")
            return
        try:
            self.__serv.adauga_spectacol(titlu,artist,gen,durata)
            print("Spectacol adaugat cu succes")
        except ValueError as e:
            print(e)

    def __modifica_spectacol_ui(self):
        titlu=input("Introduceti titlu:")
        artist=input("Introduceti artist:")
        gen=input("Introduceti genul:")
        durata=input("Introduceti durata:")
        try:
            durata=int(durata)
        except ValueError:
            print("Durata trebuie sa fie numar intreg")
            return
        try:
            self.__serv.modifica_spectacol(titlu,artist,gen,durata)
            print("Spectacol modificat cu succes")
        except ValueError as e:
            print(e)
    def __exporta_spectacole_ui(self):
        file_name=input("Introduceti numele fisierului:")
        self.__serv.exporta_spectacole(file_name)

    def __adauga_spectacole_generate_ui(self):
        nr_spectacole=int(input("Introduceti numarul de spectacole:"))
        spectacole_generate=self.__serv.adauga_aleator(nr_spectacole)
        for spectacol in spectacole_generate:
            print(spectacol)

    def run(self):
        while True:
            self.print_menu()
            user_input=input("Introduceti optiunea:").strip().upper()
            match user_input:
                case "1":
                    self.__adauga_spectacol_ui()
                case "2":
                    self.__modifica_spectacol_ui()
                case "3":
                    self.__adauga_spectacole_generate_ui()
                case "4":
                    self.__exporta_spectacole_ui()
                case "E":
                    break