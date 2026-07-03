from colorama import Fore, Style

class ClientMenu:
    def __init__(self,client_service):
        self.__serv=client_service

    @staticmethod
    def print_menu():
        print("1.Adauga client")
        print("2.Cauta client")
        print("3.Sterge client")
        print("4.Actualizare client")
        print("P.Afisare lista de clienti")
        print("D.Default")
        print("E.Inapoi")

    def client_info_input(self):
        id=input("Introduceti ID-ul clientului:").strip()
        name=input("Introduceti numele clientului:").strip()
        cnp=input("Introduceti CNP-ul clientului:").strip()
        return id,name,cnp

    def add_client_ui(self):
        id,name,cnp=self.client_info_input()
        try:
            id=int(id)
        except ValueError:
            print(Fore.RED + "ID-ul este invalid" + Style.RESET_ALL)
            return
        try:
            self.__serv.add_client(id,name,cnp)
            print(Fore.GREEN + "Client adaugat cu succes" + Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED + str(e) + Style.RESET_ALL)

    def search_client_ui(self):
        id=input("Introduceti ID-ul clientului pe care doriti sa il cautati:").strip()
        try:
            id=int(id)
            searched_client = self.__serv.find_client(id)
            if searched_client is not None:
                print(searched_client)
            else:
                print(Fore.RED+f"Clientul cu id-ul [{id}] nu exista"+Style.RESET_ALL)
        except ValueError:
            print(Fore.RED + "ID-ul este invalid" + Style.RESET_ALL)

    def delete_client_ui(self):
        id=input("Introduceti ID-ul clientului pe care doriti sa il stergeti:").strip()
        try:
            id=int(id)
        except ValueError:
            print(Fore.RED + "ID-ul este invalid" + Style.RESET_ALL)
        try:
            self.__serv.delete_client(id)
            print(Fore.RED+"Client sters cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED + str(e) + Style.RESET_ALL)

    def update_client_ui(self):
        id,new_name,new_cnp=self.client_info_input()
        try:
            self.__serv.update_client(id,new_name,new_cnp)
            print(Fore.GREEN+"Client actualizat cu succes"+Style.RESET_ALL)
        except ValueError as e:
            print(Fore.RED + str(e) + Style.RESET_ALL)

    def print_clients_ui(self,clients):
        for client in clients:
            print(client)

    def run(self):
        is_running=True
        while is_running:
            self.print_menu()
            user_input=input(">>>").upper().strip()
            match user_input:
                case "1":
                    self.add_client_ui()
                case "2":
                    self.search_client_ui()
                case "3":
                    self.delete_client_ui()
                case "4":
                    self.update_client_ui()
                case "P":
                    self.print_clients_ui(self.__serv.get_all())
                case "D":
                    self.__serv.add_default()
                    print(Fore.GREEN+"S-au adaugat clientii default"+Style.RESET_ALL)
                case "E":
                    is_running=False
                case _:
                    print(Fore.RED+"Optiune invalida"+Style.RESET_ALL)
