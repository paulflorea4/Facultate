from colorama import Fore, Style


class Console:
    def __init__(self, song_service):
        self.__serv = song_service

    @staticmethod
    def afiseaza_meniu():
        print("1. Adauga melodie la lista")
        print("11. Actualizeaza o melodie din lista")
        print(
            "2. Cauta melodie dupa titlu si artist " + Fore.MAGENTA + "[SEMINAR_7: not implemented]" + Style.RESET_ALL)
        print(
            "3. Stergerea unui cantec dupa titlu si artist " + Fore.MAGENTA + "[SEMINAR_7: not implemented]" + Style.RESET_ALL)
        print("4. Afiseaza toate melodiile care au durata intre doua durate date")
        print("D. Adauga melodii default")
        print("P. Afiseaza lista de melodii")
        print("E. Iesire din aplicatie")

    def citeste_info_melodie(self) -> tuple:
        id = int(input("Introduceti ID-ul melodiei:"))
        titlu = input("Introduceti titlul melodiei:")
        artist = input("Introduceti artist melodiei:")
        gen = input("Introduceti genul melodiei:")
        durata = input("Introduceti durata melodiei:")

        try:
            durata = float(durata)
        except ValueError as e:
            print(Fore.RED + str(e) + Style.RESET_ALL)
        return id, titlu, artist, gen, durata

    def afiseaza_melodii(self, lista_melodii):
        for song in lista_melodii:
            print(song)

    def filtreaza_dupa_durata_ui(self):
        durata_s = input("Limita inferioara pentru durata este: ")
        durata_f = input("Limita superioara pentru durata este: ")

        durata_s = float(durata_s)
        durata_f = float(durata_f)
        lista_filtrata = self.__serv.filtreaza_dupa_durata(durata_s, durata_f)
        if len(lista_filtrata) > 0:
            print("Melodiile care au durata intre", durata_s, "si", durata_f, "sunt:")
            self.afiseaza_melodii(lista_filtrata)
        else:
            print("Nu exista melodii cu durata intre " + Fore.CYAN + str(
                durata_s) + Style.RESET_ALL + " si " + Fore.CYAN + str(durata_f) + Style.RESET_ALL + ".")

    def adauga_melodie_ui(self):
        id, titlu, artist, gen, durata = self.citeste_info_melodie()

        try:
            self.__serv.adauga_melodie(id, titlu, artist, gen, durata)
            print(Fore.GREEN + "Adaugare realizata cu succes." + Style.RESET_ALL)

        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def modifica_melodie_ui(self):
        id, titlu_nou, artist_nou, gen_nou, durata_noua = self.citeste_info_melodie()

        try:
            self.__serv.actualizeaza_melodie(id, titlu_nou, artist_nou, gen_nou, durata_noua)
            print(Fore.GREEN + "Actualizare realizata cu succes." + Style.RESET_ALL)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def run(self):
        is_running = True
        while is_running:
            self.afiseaza_meniu()
            optiune = input(">>>").upper().strip()
            match optiune:
                case '1':
                    self.adauga_melodie_ui()
                case '11':
                    self.modifica_melodie_ui()
                case '2':
                    # cautare in lista
                    # self.cautare_melodie_ui()
                    pass
                case '3':
                    # self.sterge_melodie_ui()
                    pass
                case '4':
                    self.filtreaza_dupa_durata_ui()
                    pass

                case 'P':
                    self.afiseaza_melodii(self.__serv.get_all())
                case 'D':
                    self.__serv.add_default()
                    print("S-au adaugat melodiile default.")
                case 'E':
                    is_running = False
