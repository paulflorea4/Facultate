from errors import ValidationError, RepositoryError

class UI:
    def __init__(self, service):
        self.__service = service
        self.__filtru_curent = ["a", 1]
        self.__setat = False

    def print_menu(self):
        print("1. Adauga produs")
        print("2. Sterge produs dupa cifra")
        print("3. Seteaza filtre")
        print("4. Undo stergere")

    def ui_adauga_produs(self, id, denumire, pret):
        try:
            self.__service.add_produs(id, denumire, pret)
        except ValidationError as e:
            print(e)
        except RepositoryError as e:
            print(e)

    def ui_sterge_produs(self, cifra: int):
        try:
            numar_sterse = self.__service.remove_produs(cifra)
            print(f"Numarul de produse sterse: {numar_sterse}")
        except RepositoryError as e:
            print(e)

    def ui_filtreaza_produse(self, filtru_denumire, filtru_pret):
        try:
            filtrate = self.__service.filter_produse(filtru_denumire, filtru_pret)
            self.__setat = True
            self.ui_afiseaza_produse(filtrate)
        except ValidationError as e:
            print(e)

    def ui_afiseaza_produse(self, produse):
        for produs in produse:
            print(produs.string_to_file())

    def ui_afiseaza_filtru(self):
        print(f"Filtu curent: text: {self.__filtru_curent[0]}, numar: {self.__filtru_curent[1]}")

    def ui_undo_stergere(self):
        try:
            self.__service.undo_remove()
        except RepositoryError as e:
            print(e)


    def run(self):
        while True:
            self.print_menu()
            command = input("Introduce comanda: ").strip().upper()
            match command:
                case "1":
                    try:
                        if self.__setat:
                            self.ui_afiseaza_filtru()
                            self.ui_filtreaza_produse(self.__filtru_curent[0], self.__filtru_curent[1])
                        id = input("Introduce id-ul produsului: ")
                        denumire = input("Introduce denumirea produsului: ")
                        pret = int(input("Introduce pretul produsului: "))
                        self.ui_adauga_produs(id, denumire, pret)
                    except ValueError as e:
                        print(e)

                case "2":
                    try:
                        if self.__setat:
                            self.ui_afiseaza_filtru()
                            self.ui_filtreaza_produse(self.__filtru_curent[0], self.__filtru_curent[1])
                        cifra = int(input("Introdu cifra dupa care sa stergi produsele: "))
                        self.ui_sterge_produs(cifra)
                    except ValueError as e:
                        print(e)

                case "3":
                    try:
                        filtru_denumire = input("Introduce filtrul pentru denumire: ")
                        filtru_pret = int(input("Introduce filtrul pentru pret: "))
                        self.__filtru_curent[0] = filtru_denumire
                        self.__filtru_curent[1] = filtru_pret
                        self.ui_filtreaza_produse(filtru_denumire, filtru_pret)
                    except ValueError as e:
                        print(e)

                case "4":
                        self.ui_undo_stergere()