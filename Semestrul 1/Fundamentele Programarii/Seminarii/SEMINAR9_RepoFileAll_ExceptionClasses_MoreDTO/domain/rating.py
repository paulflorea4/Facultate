# reprezinta evaluarea unei melodii de catre o persoana
#Seminar 9:
# Evaluare
#   __id_melodie
#   __cnp_persoana
#   __scor

class Evaluare:
    def __init__(self, id_melodie, cnp_persoana, scor):
        self.__id_melodie = id_melodie
        self.__cnp_persoana = cnp_persoana
        self.__scor = scor

    #echivalent cu get_id_melodie, apel: daca avem evaluare = Evaluare(...), evaluare.id_melodie
    @property
    def id_melodie(self):
        return self.__id_melodie

    @property
    def cnp_persoana(self):
        return self.__cnp_persoana

    #echivalent cu set_cnp_persoana, apel: daca avem evaluare = Evaluare(...), evaluare.cnp_persoana = cnp_nou
    @cnp_persoana.setter
    def cnp_persoana(self, new_cnp_pers):
        self.__persoana = new_cnp_pers

    @id_melodie.setter
    def id_melodie(self, new_id):
        self.__id_melodie = new_id

    @property
    def scor(self):
        return self.__scor

    @scor.setter
    def scor(self, new_scor):
        self.__scor = new_scor

    def __eq__(self, other):
        if type(self) != type(other):
            return False
        return self.__cnp_persoana == other.__cnp_persoana and self.__id_melodie == other.__id_melodie

    def __str__(self):
        return "[Evaluare]: " + str(self.__id_melodie) + " | " + str(self.__cnp_persoana) + " | Scor: " + str(self.__scor)
