class Console:
    def __init__(self,service):
        self.__service = service

    @staticmethod
    def print_menu():
        print("1.Adauga produs")
        print("2.Sterge produse")
        print("3.Undo")
        print("P.Print")
        print("E.Exit")

    def adauga_produs_ui(self):
        id=input("Introduceti id-ul:")
        denumire=input("Introduceti denumirea:")
        pret=input("Introduceti pretul:")
        try:
            pret=int(pret)
            self.__service.adaugare_produs(id,denumire,pret)
            print("Produs adaugat cu succes")
            print()
        except ValueError as error:
            print(error)
            print()

    def sterge_produse_ui(self):
        cifra=input("Introduceti cifra:")
        try:
            cifra=int(cifra)
            produse_sterse=self.__service.stergere_produse(cifra)
            print("Produse sterse:"+str(produse_sterse),end='\n\n')
        except ValueError as error:
            print(error)
            print()

    def undo_ui(self):
        try:
            self.__service.undo()
        except ValueError as error:
            print(error)
            print()

    def print_produse_ui(self):
        produse=self.__service.get_produse()
        for produs in produse:
            print(produs)
        print()

    def run(self):
        while True:
            self.print_menu()
            user_input=input("Introduceti optiunea:").strip().upper()
            print()
            match user_input:
                case "1":
                    self.adauga_produs_ui()
                case "2":
                    self.sterge_produse_ui()
                case "3":
                    self.undo_ui()
                case "P":
                    self.print_produse_ui()
                case "E":
                    break