import copy
from domain.produs import Produs
from errors import RepositoryError

class RepoProduse:
    def __init__(self, path):
        self.__produse = []
        self.__undo_list = []
        self.__path = path
        self.read_from_file()

    def read_from_file(self):
        with open(self.__path, "r") as f:
            for linie in f:
                linie = linie.strip()
                if linie != "":
                    parti = linie.split(",")
                    id = parti[0]
                    denumire = parti[1]
                    pret = int(parti[2])
                    produs = Produs(id, denumire, pret)
                    try:
                        self.__produse.append(produs)
                    except RepositoryError:
                        continue

    def write_to_file(self):
        with open(self.__path, "w") as f:
            produse = self.ia_all()
            for produs in produse:
                f.write(produs.string_to_file()+'\n')

    def adauga_produs(self, produs_nou):
        produse = self.ia_all()
        for produs in produse:
            if produs.id() == produs_nou.id():
                raise RepositoryError("Deja exista un produs cu acest id")
        self.__produse.append(produs_nou)
        self.write_to_file()

    def sterge_produs(self, cifra):
        produse = self.ia_all()
        contor = 0
        for produs in produse[:]:
            if str(cifra) in produs.id():
                self.__undo_list.append(copy.deepcopy(self.__produse))
                self.__produse.remove(produs)
                contor += 1
        self.write_to_file()
        return contor

    def undo_stergere(self):
        if self.__undo_list != []:
            self.__produse = self.__undo_list[-1]
            self.__undo_list.pop()
            self.write_to_file()
        else:
            raise RepositoryError("Nu se mai poate da undo")



    def ia_all(self):
        return self.__produse