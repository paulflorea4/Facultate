from domain.rating import Evaluare
from domain.dtos import SongListensV2, SongRating
from exceptions.exceptions import RepositoryException, RatingAlreadyExistsException


class RatingMemoryRepository:
    def __init__(self):
        self.__ratings = []

    def find(self, rating):
        """
        Cauta un rating in colectia de rating-uri
        :param rating: rating-ul care este cautat
        :return: True daca un rating realizat de aceeasi persoana si pentru aceeasi melodie ca
                    si in cel dat ca parametru exista deja in colectie
                 False altfel
        """
        for existing_rating in self.__ratings:
            if rating == existing_rating:
                return True
        return False

    def store(self, rating):
        """
        Adauga un rating in colectia de rating-uri
        :param rating: rating-ul de adaugat
        :return: -; colectia se modifica prin adaugarea rating-ului
        :raises: ValueError daca exista deja o evaluare pentru melodia si persoana data
        """
        if self.find(rating):
            raise RatingAlreadyExistsException()
        self.__ratings.append(rating)

    def get_all(self):
        """
        Returneaza intreaga colectie de rating-uri
        """
        return self.__ratings

    def get_all_for_song(self, id_melodie):
        """
        Returneaza evaluarile pentru melodia cu id-ul id_melodie
        :param id_melodie: id-ul melodiei pentru care se cauta evaluarile
        :return: lista de obiecte SongRating
        """
        ratings_for_song = []
        for rating in [rating for rating in self.get_all() if rating.id_melodie == id_melodie]:
            r = SongRating(rating.cnp_persoana, rating.scor)
            ratings_for_song.append(r)
        return ratings_for_song

    def get_listen_counts(self):
        """
        Calculeaza numarul de evaluari pentru fiecare melodie
        :return: lista de obiecte SongListensV2
        """
        all_ratings = self.get_all()
        listens_dict = {}
        for rating in all_ratings:
            if rating.id_melodie not in listens_dict:
                listens_dict[rating.id_melodie] = SongListensV2(rating.id_melodie, 1)
            else:
                listens_dict[rating.id_melodie].nr_ascultari += 1
        return listens_dict.values()


class RatingFileRepository(RatingMemoryRepository):
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
            with open(self.__filename, mode="r", encoding="utf-8") as file:
                lines = file.readlines()
                for line in lines:
                    line = line.strip()
                    if line:
                        idm, cnp_persoana, scor = line.split(",")
                        m = Evaluare(int(idm), cnp_persoana, float(scor))
                        RatingMemoryRepository.store(self, m)
        except IOError:
            raise RepositoryException("Nu s-a putut citi din fisierul: " + self.__filename)

    def __save_to_file(self):
        """
        Salveaza datele in fisier
        :return: -; fisierul cu numele self.__filename va contine datele in formatul specificat
        :raises: RepositoryException daca exista probleme la scrierea in fisier
        """
        try:
            with open(self.__filename, "w", encoding="utf-8") as file:
                for evaluare in super().get_all():
                    evaluare_str = str(evaluare.id_melodie) + "," + evaluare.cnp_persoana + ',' + str(
                        evaluare.scor)

                    evaluare_str += "\n"
                    file.write(evaluare_str)
        except IOError:
            raise RepositoryException("Nu s-au putut salva datele in fisierul: " + self.__filename)

    def store(self, rating):
        super().store(rating)
        self.__save_to_file()
