from domain.melodie import Melodie
from domain.validation import ValidatorMelodie
from repository.repo_melodii import SongMemoryRepository


class ServiceMelodii:
    def __init__(self, repo: SongMemoryRepository, validator: ValidatorMelodie):
        self.__repo = repo
        self.__validator = validator

    def adauga_melodie(self, id, titlu, artist, gen, durata):
        """
        Adauga o melodie
        :param id: id-ul melodiei de adaugat
        :param titlu: titlul melodiei pe care vrem sa o adaugam
        :param artist: artistul melodiei pe care vrem sa o adaugam
        :param gen: genul melodiei pe care vrem sa o adaugam
        :param durata: durata melodiei pe care vrem sa o adaugam
        :return: -; lista data se modifica prin adaugarea melodiei cu informatiile date
        :raises: ValueError daca melodia nu este valida
                 ValueError daca exista deja melodie cu id dat
        """
        m = Melodie(id, titlu, artist, gen, durata)
        self.__validator.validate(m)
        self.__repo.store(m)

    def actualizeaza_melodie(self, id: int, titlu_nou: str, artist_nou: str, gen_nou: str, durata_noua: float):
        """
        Actualizeaza melodie cu id-ul id cu informatiile date
        :param id: id-ul melodiei de actualizat
        :param titlu_nou: noul titlu al melodiei
        :param artist_nou: noul artist al melodiei
        :param gen_nou: noul gen al melodiei
        :param durata_noua: noua durata a melodiei
        :return: -; lista data se modifica prin actualizarea melodiei cu id id cu informatiile date,
                    daca o melodie cu acest id exista
        :raises: ValueError daca din informatiile date nu se poate construi o melodie valida
                 ValueError daca nu exista melodie cu id dat
        """

        #Cum am putea face daca nu dorim sa modificam toate atributele unei melodii
        #in cadrul unei actualizari?

        m_new = Melodie(id, titlu_nou, artist_nou, gen_nou, durata_noua)
        self.__validator.validate(m_new)
        self.__repo.update(m_new)

    def find_melodie(self, id: int):
        """
        Cauta melodia cu id dat
        :param id: id-ul dupa care se cauta
        :return: melodia cu id-ul dat, daca aceasta exista, None altfel
        """
        return self.__repo.find(id)

    def delete_melodie(self, id):
        pass

    def filtreaza_dupa_durata(self, durata_minima: float, durata_maxima: float) -> list:
        """
        Returneaza lista de melodii care au durata in intervalul dat
        :param durata_inceput: limita inferioara a duratei
        :param durata_final: limita superioara a duratei
        :return: lista de melodii care au durata intre durata_inceput si durata_final
        """

        lista_filtrata = []
        for elem in self.__repo.get_all():
            if durata_minima < elem.get_durata() < durata_maxima:
                lista_filtrata.append(elem)
        return lista_filtrata

    def add_default(self):
        self.adauga_melodie(101, "Perfect Strangers", "Deep Purple", "rock", 5.11)
        self.adauga_melodie(102, "Comfortably Numb", "Pink Floyd", "rock", 8.2)
        self.adauga_melodie(103, "Lose yourself", "Eminem", "hip-hop", 4.01)
        self.adauga_melodie(104, "Pe Corso", "Pasarea Colibri", "folk", 3.23)

    def get_all(self) -> list:
        """
        Returneaza colectia de melodii
        :return:
        """
        return self.__repo.get_all()
