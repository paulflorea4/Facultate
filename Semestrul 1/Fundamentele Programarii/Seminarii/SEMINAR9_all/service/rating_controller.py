from domain.rating import Evaluare
from domain.dtos import SongListens


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
        :raises: ValueError daca nu exista melodia cu id_melodie
                 ValueError daca nu exista persoana cu CNP cnp_persoana
                 ValueError daca rating-ul este invalid (scorul evaluarii este invalid)
                 ValueError daca mai exista o evaluare pentru melodia data realizata de persoana cu cnp_persoana
        """
        melodie = self.__repo_melodii.find(id_melodie)
        if melodie is None:
            raise ValueError("Nu exista melodie cu ID dat.")
        person = self.__repo_persoane.find(cnp_persoana)
        if person is None:
            raise ValueError("Nu exista persoana cu CNP dat.")

        evaluare = Evaluare(melodie, person, scor_evaluare)
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
        listens_dict = {}
        all_ratings = self.__repo_ratings.get_all()
        for rating in all_ratings:
            if rating.melodie.get_id() not in listens_dict:
                listens_dict[rating.melodie.get_id()] = SongListens(rating.melodie.get_titlu(),
                                                                    rating.melodie.get_artist(), 1)
                # {'titlu_melodie': rating.melodie.get_titlu(),
                #                                      'artist_melodie': rating.melodie.get_artist(),
                #                                      'nr_ascultari': 1}
            else:
                # listens_dict[rating.melodie.get_id()]['nr_ascultari'] += 1
                # current_dto = listens_dict[rating.melodie.get_id()]
                # current_dto.set_nr_ascultari(current_dto.get_nr_ascultari() + 1)
                listens_dict[rating.melodie.get_id()].incrementeaza_nr_ascultari()

        #Seminar 9:
        # cheie = id_melodie
        # valoare = obiect de tip SongListens (DTO) cu campurile titlu, artist, nr_ascultari
        dto_list = listens_dict.values()
        # added from seminar
        # sortare dupa nr_ascultari ca prim criteriu,
        # dar daca nu avem numar de ascultari egale, sorteaza dupa artist,
        # iar daca si artistul e acelasi, sorteaza dupa titlu
        #Initial: lambda dto: dto.get_nr_ascultari()
        #Modificat: lambda dto: (dto.get_nr_ascultari(), dto.get_artist(), dto.get_titlu())
        dto_list = sorted(dto_list, key = lambda dto: (dto.get_nr_ascultari(), dto.get_artist(), dto.get_titlu()), reverse=True)
        dto_list = dto_list[:n]
        return dto_list

    def best_rated_in_genre(self, genre):
        all_ratings = self.__repo_ratings.get_all()
        # ratings_to_work_with = [rating for rating in all_ratings if rating.melodie.get_gen() == genre]
        pass

    def get_all(self):
        return self.__repo_ratings.get_all()
