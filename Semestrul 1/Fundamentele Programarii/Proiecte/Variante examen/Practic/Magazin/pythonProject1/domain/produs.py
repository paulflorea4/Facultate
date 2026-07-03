class Produs:
    def __init__(self, id:str,denumire:str,pret:int):
        self.__id = id
        self.__denumire = denumire
        self.__pret = pret

    def id(self):
        return self.__id

    def denumire(self):
        return self.__denumire

    def pret(self):
        return self.__pret

    def __str__(self):
        return self.__id+","+self.__denumire+","+str(self.__pret)