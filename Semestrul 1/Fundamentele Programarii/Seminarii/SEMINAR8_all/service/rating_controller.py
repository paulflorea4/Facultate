from collections import defaultdict

from domain.rating import Evaluare


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
                listens_dict[rating.melodie.get_id()] = {'titlu_melodie': rating.melodie.get_titlu(),
                                                         'artist_melodie': rating.melodie.get_artist(),
                                                         'nr_ascultari': 1}
            else:
                listens_dict[rating.melodie.get_id()]['nr_ascultari'] += 1

        # TO-DO: sort and take first n
        # sort by 'nr_ascultari' descending
        # remember how we did list comprehensions
        # lst = [1,2,3,4]
        # lst_pare = [x for x in lst if x%2==0] -> creeaza o noua lista cu acei x din lst pentru care
        #               x%2==0 este true
        # aici:
        #    listens_dict_sorted este un nou dictionar cu cheie k asociata cu valoare v,
        #    k si v se iau dintr-o lista de tupluri sortate
        #    exemplu cu mai putine valori/dictionar mai simplu
        #    d = {'a':5, 'b':1, 'c':3, 'd':8}
        #    d.items() este [('a',  5), ('b', 1), ('c', 3), ('d', 8)]
        #    sorted(d.items(), key = lambda x: x[1]) sorteaza lista de tupluri d.items()
        #          considerand cheia ca fiind a doua valoare din tuplu
        #          key = lambda x: x[1] imi spune ca voi ordona tuplurile in functie
        #          de valoarea de pe pozitia 1 din tuplu, care reprezinta in acest caz
        #          valoarea numerica (key in acest context = campul dupa care se sorteaza)
        #   deci sorted(d.items(), key = lambda x: x[1]) va returna
        #   [('b', 1), ('c', 3), ('a', 5), ('d', 8)]
        #   pe noi ne intereseaza sa sortam descrescator dupa numar de ascultari
        #   deci reverse = True
        #   [('d', 8), ('a', 5), ('c', 3), ('b', 1)]

        # sorted_tuple_list = sorted(listens_dict.items(), key=lambda item: item[1]['nr_ascultari'], reverse=True)
        # #from the sorted list, take only the first n
        # most_listened_to_n_songs = sorted_tuple_list[:n]
        # listens_dict_sorted = {k: v for k, v in most_listened_to_n_songs}

        return listens_dict

    def best_rated_in_genre(self, genre):
        all_ratings = self.__repo_ratings.get_all()
        # ratings_to_work_with = [rating for rating in all_ratings if rating.melodie.get_gen() == genre]
        pass

    def get_all(self):
        return self.__repo_ratings.get_all()
