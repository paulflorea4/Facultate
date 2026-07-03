from domain.melodie import Melodie

#Repo in memorie
class RepositoryMelodii:
    def __init__(self):
        self.__elements = []

    def find(self, id: int)->Melodie:
        """
        Cauta melodia cu id dat
        :param id: id-ul cautat
        :return: obiect de tip Melodie daca exista melodie cu id dat, None altfel
        """
        for melodie in self.__elements:
            if melodie.get_id() == id:
                return melodie
        return None

    def store(self, melodie: Melodie):
        """
        Adauga o melodie la colectia de melodii
        :param melodie: melodia de adaugat
        :return: -; colectia de melodii se modifica prin adaugarea melodiei date
                postconditie: melodie apartine colectiei de melodii
        :raises: ValueError daca se incearca adaugarea unei melodii cu id care exista deja
        """
        if self.find(melodie.get_id()) is not None:
            raise ValueError("Exista deja melodie cu acest id.")
        self.__elements.append(melodie)

    #metoda pe care o folosim doar intern, in cadrul clasei (could have also used _)
    def __find_pos(self, id: int):
        """
        Gaseste pozitia in lista a melodiei cu id dat (daca o astfel de melodie exista)
        :param id: id-ul cautat
        :return: pozitia in lista a melodiei cu id dat, pos returnat intre 0 si len(self.__elements) daca melodia exista
                -1 daca nu exista melodie cu id dat
        """
        pos = -1
        for index, melodie in enumerate(self.__elements):
            if melodie.get_id() == id:
                pos = index
                break
        return pos

    def update(self, melodie_actualizata):
        """
        Actualizeaza melodia din lista cu ID = id-ul melodiei date ca parametru
        :param melodie_actualizata: melodia actualizata
        :return:
        """
        pos = self.__find_pos(melodie_actualizata.get_id())
        if pos == -1:
            raise ValueError("Nu exista melodie cu id dat.")
        self.__elements[pos] = melodie_actualizata

    def delete(self, id: int):
        pass

    def get_all(self) -> list:
        """
        Returneaza colectia de melodii
        :return: colectia de melodii
        """
        return self.__elements

    def get_size(self)->int:
        return len(self.__elements)