# reprezinta evaluarea unei melodii de catre o persoana
# Atribute:
#   melodie: Melodie (melodia care se evalueaza)
#   persoana: Persoana (persoana care evalueaza)
#   scor: float, scorul evaluarii, intre 1 si 5

#Seminar 9:
# Evaluare
#   __id_melodie
#   __cnp_persoana
#   __scor

class Evaluare:
    def __init__(self, melodie, persoana, scor):
        self.__melodie = melodie
        self.__persoana = persoana
        self.__scor = scor

    @property
    def melodie(self):
        return self.__melodie

    @property
    def persoana(self):
        return self.__persoana

    @persoana.setter
    def persoana(self, new_pers):
        self.__persoana = new_pers

    @property
    def scor(self):
        return self.__scor

    def __eq__(self, other):
        if type(self) != type(other):
            return False
        return self.__persoana == other.persoana and self.__melodie == other.melodie

    def __str__(self):
        return "[Evaluare]: " + str(self.__melodie) + " | " + str(self.__persoana) + " | Scor: " + str(self.__scor)
