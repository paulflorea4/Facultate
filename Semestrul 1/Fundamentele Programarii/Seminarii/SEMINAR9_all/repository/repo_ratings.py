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
            raise ValueError("Exista deja evaluare pentru melodia si persoana data.")
        self.__ratings.append(rating)

    def get_all(self):
        """
        Returneaza intreaga colectie de rating-uri
        """
        return self.__ratings
