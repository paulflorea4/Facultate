class Melodie:
    def __init__(self, titlu: str, artist: str, gen: str, durata: float):
        self.__titlu = titlu
        self.__artist = artist
        self.__gen = gen
        self.__durata = durata

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
        return self.__titlu == other.__titlu and self.__artist == other.__artist

    def __str__(self):
        return "Melodie: Titlu = " + self.__titlu + "; Artist = " + self.__artist + "; Gen = " + self.__gen + "; Durata = " + str(
            self.__durata)
    # @property
    # def artist(self):
    #     return self.__artist
    #
    # @artist.setter
    # def artist(self, new_value):
    #     # eventual validare
    #     self.__artist = new_value
    #
    # @property
    # def gen(self):
    #     return self.__gen
    #
    # @gen.setter
    # def gen(self, new_value):
    #     # eventual validare
    #     self.__gen = new_value
    #
    # @property
    # def durata(self):
    #     return self.__durata
    #
    # @durata.setter
    # def artist(self, new_value):
    #     # eventual validare
    #     self.__durata = new_value

# Consideram obiectul m = Melodie("Titlu1", "Artist1", "pop", 3.44)

# In varianta cu get, set "simplu", trebuie sa apelam m.get_titlu()
# sau m.set_titlu("Alt titlu")

# In varianta cu @property, putem face m.titlu (e.g. print(m.titlu)) pentru
# accesa valoarea atributului (get) si m.titlu = "Alt titlu" (set) si se vor apela cele
# doua metode - prima (cea cu @property) cand incercam sa accesam valoarea atributului titlu,
# iar a doua (cea cu @titlu.setter) cand dorim sa schimbam valoarea atributului titlu pentru o melodie
# printr-o instructiune de tip m.titlu = "abc"
# https://www.freecodecamp.org/news/python-property-decorator/
