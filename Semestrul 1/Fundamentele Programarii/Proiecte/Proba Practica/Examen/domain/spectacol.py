class Spectacol:
    def __init__(self, titlu:str,artist:str,gen:str,durata:int):
        self.__titlu = titlu
        self.__artist = artist
        self.__gen = gen
        self.__durata = durata

    def titlu(self):
        return self.__titlu

    def artist(self):
        return self.__artist

    @property
    def gen(self):
        return self.__gen

    @gen.setter
    def gen(self,gen):
        self.__gen = gen

    @property
    def durata(self):
        return self.__durata

    @durata.setter
    def durata(self,durata):
        self.__durata = durata

    def __str__(self):
        return self.__titlu+","+self.__artist+","+self.__gen+","+str(self.__durata)

    def __eq__(self,other):
        if (self.titlu() == other.titlu() and self.artist() == other.artist() and
            self.gen == other.gen and self.durata == other.durata):
            return True
        return False
