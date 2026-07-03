from domain.melodie import Melodie


class MusicManager:
    def __init__(self, validator):
        self.__validator = validator
        self.__lista_melodii = []

    def adauga_melodie(self, titlu: str, artist: str, gen: str, durata: float) -> None:
        """
        Adauga o melodie la lista de melodii
        :param music_manager: manager-ul de muzica gestionata de aplicatie
        :param titlu: titlul melodiei pe care vrem sa o adaugam
        :param artist: artistul melodiei pe care vrem sa o adaugam
        :param gen: genul melodiei pe care vrem sa o adaugam
        :param durata: durata melodiei pe care vrem sa o adaugam
        :return: -; lista data se modifica prin adaugarea melodiei cu informatiile date
        :raises: ValueError daca melodia nu este valida
        """
        melodie = Melodie(titlu, artist, gen, durata)
        self.__validator.validate(melodie)
        self.__lista_melodii.append(melodie)

    def cauta_melodie(self, titlu_cautat: str, artist_cautat: str):
        """
        Cauta o melodie in lista dupa titlu si artist
        :param music_manager: manager-ul de muzica gestionata de aplicatie
        :param titlu_cautat: titlul dupa care se cauta
        :param artist_cautat: artistul dupa care se cauta
        :return: melodia cu titlul si artistul dat, daca aceasta exista in lista
                 None, altfel
        """

        for melodie in self.__lista_melodii:
            if melodie.get_titlu() == titlu_cautat and melodie.get_artist() == artist_cautat:
                return melodie
        return None

    def sterge_melodie(self, titlu: str, artist: str):
        """
        Sterge melodia cu titlul si artistul dat, daca aceasta exista in lista
        :param music_manager: manager-ul de muzica gestionata de aplicatie
        :param titlu: titlul melodiei pentru care se incearca stergerea
        :param artist: artistul melodiei pentru care se incearca stergerea
        :return: -; lista_melodii se modifica prin stergerea melodiei cu titlu, artist dat,
                    daca aceasta melodie exista; altfel, lista ramane nemodificata
        """
        melodie_cautata = self.cauta_melodie(titlu, artist)
        if melodie_cautata is not None:
            self.__lista_melodii.remove(melodie_cautata)

    def filtreaza_dupa_durata(self, durata_inceput: float, durata_final: float) -> list:
        """
        Returneaza o lista de melodii care au durata in intervalul dat
        :param music_manager: manager-ul care contine atat lista de melodii, cat si lista de undo
        :param durata_inceput: limita inferioara a duratei
        :param durata_final: limita superioara a duratei
        :return: lista de melodii care au durata intre durata_inceput si durata_final
        """

        lista_noua = []
        for elem in self.__lista_melodii:
            if durata_inceput < elem.get_durata() < durata_final:
                lista_noua.append(elem)

        return lista_noua

    def add_default_songs(self):
        self.adauga_melodie("Titlu1", "Artist1", "rock", 3.41)
        self.adauga_melodie("Titlu2", "Artist2", "folk", 5.01)
        self.adauga_melodie("Titlu3", "Artist3", "folk", 2.33)
        self.adauga_melodie("Titlu4", "Artist4", "pop", 1.56)
        self.adauga_melodie("Titlu5", "Artist5", "rock", 13.02)

    def get_melodii(self):
        return self.__lista_melodii