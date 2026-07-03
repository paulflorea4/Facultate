from colorama import Fore, Style


class Console:
    def __init__(self, music_manager):
        self.__manager = music_manager

    @staticmethod
    def afiseaza_meniu():
        print("1. Adauga melodie la lista")
        print("2. Cauta melodie dupa titlu si artist")
        print("3. Stergerea unui cantec dupa titlu si artist")
        print("4. Afiseaza toate melodiile care au durata intre doua durate date")
        print("D. Adauga melodii default")
        print("P. Afiseaza lista de melodii")
        print("E. Iesire din aplicatie")

    def citeste_info_melodie(self) -> tuple:
        titlu = input("Introduceti titlul melodiei:")
        artist = input("Introduceti artist melodiei:")
        gen = input("Introduceti genul melodiei:")
        durata = input("Introduceti durata melodiei:")

        try:
            durata = float(durata)
        except ValueError as e:
            print(Fore.RED + str(e) + Style.RESET_ALL)
        return titlu, artist, gen, durata

    def afiseaza_melodii(self, lista_melodii):
        for song in lista_melodii:
            print(song)


    def filtreaza_dupa_durata_ui(self):
        durata_s = input("Limita inferioara pentru durata este: ")
        durata_f = input("Limita superioara pentru durata este: ")

        durata_s = float(durata_s)
        durata_f = float(durata_f)
        lista_filtrata = self.__manager.filtreaza_dupa_durata(durata_s, durata_f)
        print("Melodiile care au durata intre", durata_s, "si", durata_f, "sunt:")
        self.afiseaza_melodii(lista_filtrata)



    def adauga_melodie_ui(self):
        titlu, artist, gen, durata = self.citeste_info_melodie()

        try:
            self.__manager.adauga_melodie(titlu, artist, gen, durata)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def cautare_melodie_ui(self):
        titlu_cautat = input("Titlul melodiei cautate: ")
        artist_cautat = input("Artistul melodiei cautate: ")
        melodie_cautata = self.__manager.cauta_melodie(titlu_cautat, artist_cautat)
        if melodie_cautata is not None:
            print("Melodia a fost gasita, acestea sunt toate informatiile despre ea:", melodie_cautata)
        else:
            print("Melodia nu a fost gasita in lista.")

    def sterge_melodie_ui(self):
        titlu_de_sters = input("Titlul melodiei de sters:")
        artist_de_sters = input("Artistul melodiei de sters: ")
        self.__manager.sterge_melodie(titlu_de_sters, artist_de_sters)

    def run(self):
        is_running = True
        while is_running:
            self.afiseaza_meniu()
            optiune = input(">>>").upper().strip()
            match optiune:
                case '1':
                    self.adauga_melodie_ui()
                case '2':
                    # cautare in lista
                    self.cautare_melodie_ui()
                case '3':
                    self.sterge_melodie_ui()
                case '4':
                    self.filtreaza_dupa_durata_ui()
                case 'P':
                    self.afiseaza_melodii(self.__manager.get_melodii())
                case 'D':
                    self.__manager.add_default_songs()
                    print("S-au adaugat melodiile default.")
                case 'E':
                    is_running = False
