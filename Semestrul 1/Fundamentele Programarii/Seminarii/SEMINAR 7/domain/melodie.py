class Melodie:
    def __init__(self, id: int, titlu: str, artist: str, gen: str, durata: float):
        self.__id = id
        self.__titlu = titlu
        self.__artist = artist
        self.__gen = gen
        self.__durata = durata

    def get_id(self):
        return self.__id

    def get_titlu(self):
        return self.__titlu

    # echivalent cu:
    @property
    def titlu(self):
        return self.__titlu

    def set_titlu(self, new_value):
        self.__titlu = new_value

    # echivalent cu:
    @titlu.setter
    def titlu(self, new_value):
        if not isinstance(new_value, str):
            raise ValueError("Titlul poate fi doar de tip str.")
        self.__titlu = new_value

    def get_artist(self):
        return self.__artist

    def set_artist(self, value):
        self.__artist = value

    def get_gen(self):
        return self.__gen

    def set_gen(self, value):
        self.__gen = value

    def get_durata(self):
        return self.__durata

    def set_durata(self, value):
        self.__durata = value

    def __eq__(self, other):
        if type(self) != type(other):
            return False
        return self.__id == other.__id

    def __str__(self):
        return "[" + str(
            self.__id) + "] Melodie: Titlu = " + self.__titlu + "; Artist = " + self.__artist + "; Gen = " + self.__gen + "; Durata = " + str(
            self.__durata)
