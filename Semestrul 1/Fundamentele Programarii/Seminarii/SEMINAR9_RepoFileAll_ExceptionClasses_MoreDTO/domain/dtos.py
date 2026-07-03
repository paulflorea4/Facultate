class SongListensV2:
    def __init__(self, id_melodie, nr_ascultari):
        self.__id = id_melodie
        self.__titlu = ""
        self.__artist = ""
        self.__nr_ascultari = nr_ascultari

    @property
    def id_melodie(self):
        return self.__id

    @property
    def titlu(self):
        return self.__titlu

    @property
    def artist(self):
        return self.__artist

    @property
    def nr_ascultari(self):
        return self.__nr_ascultari

    @titlu.setter
    def titlu(self, titlu_nou):
        self.__titlu = titlu_nou

    @artist.setter
    def artist(self, artist_nou):
        self.__artist = artist_nou

    @nr_ascultari.setter
    def nr_ascultari(self, nr_ascultari):
        self.__nr_ascultari = nr_ascultari

    def __str__(self):
        return "Titlu: " + self.__titlu + "| Artist: " + self.__artist + " | Nr. ascultari: " + str(self.__nr_ascultari)


class SongListensV1:
    def __init__(self, titlu, artist, nr_ascultari=1):
        self.__titlu = titlu
        self.__artist = artist
        self.__nr_ascultari = nr_ascultari

    @property
    def titlu(self):
        return self.__titlu

    @property
    def artist(self):
        return self.__artist

    @property
    def nr_ascultari(self):
        return self.__nr_ascultari

    @titlu.setter
    def titlu(self, titlu_nou):
        self.__titlu = titlu_nou

    @artist.setter
    def artist(self, artist_nou):
        self.__artist = artist_nou

    @nr_ascultari.setter
    def nr_ascultari(self, nr_ascultari):
        self.__nr_ascultari = nr_ascultari

    def incrementeaza_nr_ascultari(self):
        self.__nr_ascultari += 1

    def __str__(self):
        return "Titlu: " + self.__titlu + "| Artist: " + self.__artist + " | Nr. ascultari: " + str(self.__nr_ascultari)


class SongRating:
    def __init__(self, cnp_persoana, scor):
        self.__person_cnp = cnp_persoana
        self.__person_name = ""
        self.__scor = scor

    @property
    def person_cnp(self):
        return self.__person_cnp

    @property
    def person_name(self):
        return self.__person_name

    @property
    def scor(self):
        return self.__scor

    @person_name.setter
    def person_name(self, new_name):
        self.__person_name = new_name

    @scor.setter
    def scor(self, new_scor):
        self.__scor = new_scor

    def __str__(self):
        return self.__person_name + " a evaluat melodia cu scorul: " + str(self.__scor)
