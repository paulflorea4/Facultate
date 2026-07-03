from domain.produs import Produs
from domain.validator import ValidatorProdus
from repository.repo_produse import RepoProduse


class ServiceProduse:
    def __init__(self, repo:RepoProduse,validator:ValidatorProdus):
        self.__repo = repo
        self.__validator = validator

    def adaugare_produs(self,id:str,denumire:str,pret:int):
        produs=Produs(id,denumire,pret)
        self.__validator.valideaza(produs)
        self.__repo.store(produs)

    def stergere_produse(self,cifra:int):
        produse_sterse=0
        produse=self.__repo.get_produse()
        for produs in produse[:]:
            if str(cifra) in produs.id():
                self.__repo.delete(produs.id())
                produse_sterse+=1
        return produse_sterse

    def undo(self):
        self.__repo.undo()

    def get_produse(self):
         return self.__repo.get_produse()