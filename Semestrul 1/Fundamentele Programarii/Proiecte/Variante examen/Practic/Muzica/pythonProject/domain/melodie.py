class Melodie:
    def __init__(self, titlu:str,artist:str,gen:str,durata:int):
        self.__titlu = titlu
        self.__artist = artist
        self.__gen = gen
        self.__durata = durata

    def get_titlu(self):
        return self.__titlu

    def get_artist(self):
        return self.__artist

    @property
    def gen(self):
        return self.__gen

    @gen.setter
    def gen(self,gen_nou):
        self.__gen = gen_nou

    @property
    def durata(self):
        return self.__durata

    @durata.setter
    def durata(self,durata_noua):
        self.__durata = durata_noua

    def __str__(self):
        return self.__titlu+","+self.__artist+","+self.__gen+","+str(self.__durata)