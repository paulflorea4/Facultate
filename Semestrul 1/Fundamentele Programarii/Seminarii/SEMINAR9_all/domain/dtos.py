class SongListens:
    # DTO (Data Transfer Object)
    def __init__(self, titlu, artist, nr_listens):
        self.__titlu = titlu
        self.__artist = artist
        self.__nr_listens = nr_listens

    def get_titlu(self):
        return self.__titlu

    def get_artist(self):
        return self.__artist

    def get_nr_ascultari(self):
        return self.__nr_listens

    def set_titlu(self, titlu_nou):
        self.__titlu = titlu_nou

    def set_artist(self, artist_nou):
        self.__artist = artist_nou

    def set_nr_ascultari(self, nr_nou):
        self.__nr_listens = nr_nou

    def incrementeaza_nr_ascultari(self):
        self.__nr_listens += 1

    def __str__(self):
        return self.__titlu + " - " + self.__artist + ": " + str(self.__nr_listens)
