from domain.dtos import SongListensV1
from domain.rating import Evaluare
from exceptions.exceptions import PersonDoesNotExistException, SongDoesNotExistException


class ServiceRatings:
    def __init__(self, repo_melodii, repo_persoane, repo_ratings, validator_rating):
        self.__repo_melodii = repo_melodii
        self.__repo_persoane = repo_persoane
        self.__repo_ratings = repo_ratings
        self.__validator = validator_rating

    def adauga_rating(self, id_melodie, cnp_persoana, scor_evaluare):
        """
        Adauga evaluare (=rating)
        :param id_melodie: id-ul melodiei pentru care se adauga evaluare
        :param cnp_persoana: CNP-ul persoanei care realizeaza evaluarea
        :param scor_evaluare: scorul evaluarii
        :return: -; colectia de rating-uri se modifica prin adaugarea evaluarii
        :raises: SongDoesNotExistException daca nu exista melodia cu id_melodie
                 PersonDoesNotExistException daca nu exista persoana cu CNP cnp_persoana
                 ValidationException daca rating-ul este invalid (scorul evaluarii este invalid)
                 RatingAlreadyExistsException daca mai exista o evaluare pentru melodia data realizata de persoana cu cnp_persoana
        """
        melodie = self.__repo_melodii.find(id_melodie)
        if melodie is None:
            raise SongDoesNotExistException()
        person = self.__repo_persoane.find(cnp_persoana)
        if person is None:
            raise PersonDoesNotExistException()

        evaluare = Evaluare(id_melodie, cnp_persoana, scor_evaluare)
        self.__validator.validate(evaluare)
        self.__repo_ratings.store(evaluare)

    def most_listened_to(self, n=5):
        """
        Returneaza informatii despre cele mai ascultate n melodii
        :param n: numarul de melodii de afisat
        :return: dictionar cu cheie = id melodie si valoare = un alt dictionar in care
                se pastreaza titlul, artistul si numarul de ascultari al melodiei cu acel id
        """
        # 1 ascultare = 1 rating
        # Version 1 - build everything here
        # listens_dict = {}
        # all_ratings = self.__repo_ratings.get_all()
        # for rating in all_ratings:
        #     if rating.id_melodie not in listens_dict:
        #         melodie = self.__repo_melodii.find(rating.id_melodie)
        #         listens_dict[rating.id_melodie] = SongListensV1(melodie.get_titlu(), melodie.get_artist(), 1)
        #     else:
        #         listens_dict[rating.id_melodie].incrementeaza_nr_ascultari()
        #
        # dto_list = listens_dict.values()
        # sorted_dto_list = sorted(dto_list, key=lambda dto_obj: dto_obj.nr_ascultari, reverse=True)
        #most_listened_to_n_songs = sorted_dto_list[:n]

        # Version 2: take what we have from repo
        # and add the remaining information
        dto_list = self.__repo_ratings.get_listen_counts()
        sorted_dto_list = sorted(dto_list, key=lambda dto_obj: dto_obj.nr_ascultari, reverse=True)
        most_listened_to_n_songs = sorted_dto_list[:n]
        for dto in most_listened_to_n_songs:
            print(dto.id_melodie)
            melodie = self.__repo_melodii.find(dto.id_melodie)
            dto.titlu = melodie.get_titlu()
            dto.artist = melodie.get_artist()

        return most_listened_to_n_songs

    def sort_evaluations_for_song(self, song_id: int):
        """
        Returneaza evaluarile pentru melodia cu id = song_id in ordine descrescatoare a scorului
        :param song_id: id-ul melodiei pentru care se cauta evaluarile
        :return: lista de SongRating ordonata descrescator dupa scor
        """
        evaluations_for_song = self.__repo_ratings.get_all_for_song(song_id)
        evaluations_for_song = sorted(evaluations_for_song, key=lambda song_rating: song_rating.scor, reverse=True)

        for song_rating in evaluations_for_song:
            person = self.__repo_persoane.find(song_rating.person_cnp)
            song_rating.person_name = person.nume
        return evaluations_for_song

    def get_all(self):
        return self.__repo_ratings.get_all()
