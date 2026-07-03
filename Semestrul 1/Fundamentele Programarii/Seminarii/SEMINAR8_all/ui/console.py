from colorama import Fore, Style


class Console:
    def __init__(self, song_service, person_service, rating_service):
        self.__song_serv = song_service
        self.__person_service = person_service
        self.__rating_service = rating_service

    @staticmethod
    def afiseaza_meniu():
        print("Comenzile disponibile pentru gestiunea entitatilor (add _song, _person sau _rating) sunt: ")
        print(Fore.MAGENTA + "add_, find_, delete_, show_" + Style.RESET_ALL)
        print("Comenzile disponibile pentru rapoarte sunt: ")
        print(Fore.GREEN + "most_listened_to" + Style.RESET_ALL + ": afiseaza cele mai ascultate n melodii")
        # Se afiseaza: Artist - Titlu - nr. ascultari
        print(
            Fore.GREEN + "best_rated_in_genre" + Style.RESET_ALL + ": afiseaza melodiile cu cele mai bune evaluari dintr-un gen dat (primele 3)")

        print(
            Fore.GREEN + "music_addicts" + Style.RESET_ALL + ": afiseaza persoanele cu cel mai mare timp de ascultare (primii 30%)")
        print("Pentru iesire din aplicatie, comanda este " + Fore.RED + "exit" + Style.RESET_ALL + ".")

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

    def afiseaza_entitati(self, lista_entitati):
        for entitate in lista_entitati:
            print(entitate)

    def filtreaza_dupa_durata_ui(self):
        durata_s = input("Limita inferioara pentru durata este: ")
        durata_f = input("Limita superioara pentru durata este: ")

        durata_s = float(durata_s)
        durata_f = float(durata_f)
        lista_filtrata = self.__song_serv.filtreaza_dupa_durata(durata_s, durata_f)
        if len(lista_filtrata) > 0:
            print("Melodiile care au durata intre", durata_s, "si", durata_f, "sunt:")
            self.afiseaza_entitati(lista_filtrata)
        else:
            print("Nu exista melodii cu durata intre " + Fore.CYAN + str(
                durata_s) + Style.RESET_ALL + " si " + Fore.CYAN + str(durata_f) + Style.RESET_ALL + ".")

    def adauga_melodie_ui(self):
        id, titlu, artist, gen, durata = self.citeste_info_melodie()

        try:
            self.__song_serv.adauga_melodie(id, titlu, artist, gen, durata)
            print(Fore.GREEN + "Adaugare realizata cu succes." + Style.RESET_ALL)

        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def modifica_melodie_ui(self):
        id, titlu_nou, artist_nou, gen_nou, durata_noua = self.citeste_info_melodie()

        try:
            self.__song_serv.actualizeaza_melodie(id, titlu_nou, artist_nou, gen_nou, durata_noua)
            print(Fore.GREEN + "Actualizare realizata cu succes." + Style.RESET_ALL)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def run(self):
        is_running = True
        while is_running:
            self.afiseaza_meniu()
            cmd = input(">>>").lower().strip()
            match cmd:
                case 'add_song':
                    self.adauga_melodie_ui()
                case 'find_song':
                    self.cauta_melodie_ui()
                case 'delete_song':
                    self.sterge_melodie_ui()
                case 'add_default_songs':
                    self.__song_serv.add_default()
                case 'show_songs':
                    self.afiseaza_entitati(self.__song_serv.get_all())
                case 'add_person':
                    self.adauga_persoana_ui()
                case 'find_person':
                    self.cauta_persoana_ui()
                case 'delete_person':
                    self.sterge_persoana_ui()
                case 'show_persons':
                    self.afiseaza_entitati(self.__person_service.get_all())
                case 'add_rating':
                    self.__adauga_evaluare_ui()
                case 'show_ratings':
                    self.afiseaza_entitati(self.__rating_service.get_all())
                case 'most_listened_to':
                    d = self.__rating_service.most_listened_to()
                    print(d)
                case 'best_rated_in_genre':
                    pass
                case 'music_addicts':
                    pass
                case 'exit':
                    is_running = False

    def cauta_melodie_ui(self):
        try:
            id = int(input("ID cautat:"))
            melodie = self.__song_serv.find_melodie(id)
            if melodie is not None:
                print("S-a gasit urmatoarea melodie:", melodie)
            else:
                print("Nu s-a gasit melodie cu id dat.")
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def cauta_persoana_ui(self):

        cnp = input("CNP cautat:")
        person = self.__person_service.find_person(cnp)
        if person is not None:
            print("S-a gasit urmatoarea persoana:", person)
        else:
            print("Nu s-a gasit persoana cu cnp dat.")

    def sterge_melodie_ui(self):
        try:
            id = int(input("ID aferent melodiei care se sterge:"))
            self.__song_serv.delete_melodie(id)
            print(Fore.GREEN + "Melodia s-a sters cu succes." + Style.RESET_ALL)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def adauga_persoana_ui(self):
        try:
            cnp = input("CNP:")
            nume = input("Nume: ")
            self.__person_service.adauga_persoana(cnp, nume)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def sterge_persoana_ui(self):
        try:
            cnp = input("CNP aferent persoanei care se sterge:")
            deleted_person = self.__person_service.delete_persoana(cnp)
            print(Fore.GREEN + "Persoana [" + str(deleted_person) + "] s-a sters cu succes." + Style.RESET_ALL)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)

    def __adauga_evaluare_ui(self):
        try:
            id = int(input("ID melodie"))
            cnp = input("CNP persoana:")
            scor_evaluare = float(input("Rating: "))
            self.__rating_service.adauga_rating(id, cnp, scor_evaluare)
            print(Fore.GREEN + "Rating-ul s-a adaugat cu succes." + Style.RESET_ALL)
        except ValueError as ve:
            print(Fore.RED + str(ve) + Style.RESET_ALL)
