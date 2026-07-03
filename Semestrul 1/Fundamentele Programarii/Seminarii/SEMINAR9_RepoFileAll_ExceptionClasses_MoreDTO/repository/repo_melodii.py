from domain.melodie import Melodie
from exceptions.exceptions import SongAlreadyExistsException, SongDoesNotExistException, RepositoryException


# Repo in memorie
class SongMemoryRepository:
    def __init__(self):
        self.__elements = []

    def find(self, id: int) -> Melodie:
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
            raise SongAlreadyExistsException()
        self.__elements.append(melodie)

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
        :return: -; lista contine melodia modificata
        :raises: ValueError daca nu exista o melodie cu id-ul dat
        """
        pos = self.__find_pos(melodie_actualizata.get_id())
        if pos == -1:
            raise SongDoesNotExistException()
        self.__elements[pos] = melodie_actualizata

    def delete(self, id: int):
        pass

    def get_all(self) -> list:
        """
        Returneaza colectia de melodii
        """
        return self.__elements

    def get_size(self) -> int:
        return len(self.__elements)


class SongFileRepository(SongMemoryRepository):
    def __init__(self, filename):
        super().__init__()
        self.__filename = filename
        self.__load_from_file()

    def __load_from_file(self):
        """
        Incarca datele din fisier
        :return: -; datele din fisier sunt incarcate si in memorie
        :raises: RepositoryException daca exista probleme la citirea datelor din fisier
        """
        try:
            with open(self.__filename, "r") as file:
                lines = file.readlines()
                for line in lines:
                    line = line.strip()
                    if line:
                        id_melodie, titlu, artist, gen, durata = line.split(",")
                        m = Melodie(int(id_melodie), titlu.strip(), artist.strip(), gen.strip(), float(durata))
                        super().store(m)
        except IOError:
            raise RepositoryException("Nu s-au putut citi datele din fisierul:" + self.__filename)

    def store(self, melodie: Melodie):
        super().store(melodie)
        # SongMemoryRepository.store(self, melodie)
        self.__save_to_file()

    def delete(self, id: int):  
        super().delete(id)
        self.__save_to_file()

    def update(self, melodie_actualizata):
        super().update(melodie_actualizata)
        self.__save_to_file()

    def __save_to_file(self):
        """
        Salveaza datele in fisier
        :return: -; fisierul cu numele self.__filename va contine datele in formatul specificat
        :raises: RepositoryException daca exista probleme la scrierea in fisier
        """
        try:
            with open(self.__filename, "w") as file:
                melodii = super().get_all()
                # print(len(melodii))
                for melodie in melodii:
                    melodie_str = str(
                        melodie.get_id()) + "," + melodie.get_titlu() + "," + melodie.get_artist() + "," + melodie.get_gen() + "," + str(
                        melodie.get_durata())x
                    melodie_str += '\n'
                    file.write(melodie_str)
        except IOError:
            raise RepositoryException("Nu s-au putut salva datele in fisierul " + self.__filename)
