from domain.produs import Produs

class ServiceProduse:
    def __init__(self, repo, validator):
        self.__repo = repo
        self.__validator = validator

    def add_produs(self, id: str, denumire: str, pret: int):
        produs_fictiv = Produs(id, denumire, pret)

        self.__validator.valideaza(produs_fictiv)

        self.__repo.adauga_produs(produs_fictiv)

    def remove_produs(self, cifra: int):
        return self.__repo.sterge_produs(cifra)

    def get_all(self):
        return self.__repo.ia_all()

    def filter_produse(self, filtru_denumire, filtru_pret):
        produs_fictiv = Produs(str(111), filtru_denumire, filtru_pret)

        self.__validator.valideaza(produs_fictiv)

        produse = self.get_all()
        filtrata = []
        for produs in produse:
            if produs.pret() < filtru_pret and filtru_pret != -1 and filtru_denumire in produs.denumire():
                filtrata.append(produs)

        return filtrata

    def undo_remove(self):
        self.__repo.undo_stergere()
