from domain.spectacol import Spectacol
from domain.validator import Validator
from repository.RepoSpectacole import RepoSpectacole
import random

class SpectacoleService:
    def __init__(self, repo:RepoSpectacole,validator:Validator):
        self.__repo = repo
        self.__validator=validator

    def adauga_spectacol(self,titlu,artist,gen,durata):
        """
        Adauga spectacolul cu datele date ca parametri daca sunt valide
        :param titlu: titlul spectacolului
        :param artist: artistul spectacolului
        :param gen: genul spectacolului
        :param durata: durata spectacolului
        :return: -
        """
        spectacol=Spectacol(titlu,artist,gen,durata)
        self.__validator.validate(spectacol)
        self.__repo.store(spectacol)

    def modifica_spectacol(self,titlu,artist,gen,durata):
        """
        Modifica genul si durata spectacolului cu titlul si artistul dat ca parametri daca sunt valide
        :param titlu: titlul spectacolului
        :param artist: artistul spectacolului
        :param gen: genul de modificat
        :param durata: durata de modificat
        :return:
        """
        spectacol=Spectacol(titlu,artist,gen,durata)
        self.__validator.validate(spectacol)
        self.__repo.modify(spectacol)

    def get_len(self):
        """
        Returneaza numarul de spectacole din lista
        :return:
        """
        return self.__repo.get_len()

    def empty_file(self):
        """
        Sterge continutul fisierului dat
        :return:
        """
        self.__repo.empty_file()

    def adauga_aleator(self,nr_spectacole):
        """
        Adauga spectacole generate aleatoriu cu proprietatile date
        :param nr_spectacole: nr de spectacole de adaugat
        :return: -
        """
        vocale='aeiou'
        consoane='qwrtyupsdfghjklzxcvbnm'
        spectacole_generate=[]
        genuri=["Comedie","Concert","Balet","Altele"]
        for i in range(nr_spectacole):
            random.seed(i)
            lungime=random.randint(9,12)
            titlu=random.choice(vocale+consoane)
            if titlu in vocale:
                voc=0
            else:
                voc=1
            for j in range(lungime-1):
                if voc==1:
                    litera=random.choice(consoane)
                    voc=0
                else:
                    litera=random.choice(vocale)
                    voc=1
                titlu+=litera
            lungime_artist = random.randint(9, 12)
            artist = random.choice(vocale + consoane)
            if artist in vocale:
                voc = 0
            else:
                voc = 1
            for j in range(lungime_artist - 1):
                if voc == 1:
                    litera = random.choice(consoane)
                    voc=0
                else:
                    litera = random.choice(vocale)
                    voc=1
                artist += litera
            contor=random.randint(0,3)
            durata=random.randint(1,10)
            spectacol=Spectacol(titlu,artist,genuri[contor],durata)
            print(spectacol)
            try:
                self.adauga_spectacol(titlu,artist,genuri[contor],durata)
                spectacole_generate.append(spectacol)
            except ValueError:
                i-=1
            return spectacole_generate

    def exporta_spectacole(self, file_name):
        """
        Exporta spectacolele sortate dupa autor si titlu intr-un fisier text
        :param file_name: fisierul in care se exporta
        :return: lista de spectacole sortata
        """
        spectacole=self.__repo.get_all()
        spectacole_dict={}
        ind=0
        for spectacol in spectacole:
            spectacole_dict[ind]=[spectacol.titlu(),spectacol.artist(),spectacol.gen,spectacol.durata]
            ind+=1
        spectacole_dict=sorted(spectacole_dict.items(),key=lambda x:x[1][0],reverse=False)
        with open(file_name,'w') as file:
            for ind,spectacol in spectacole_dict:
                txt=spectacol[1]+','+spectacol[0]+','+spectacol[2]+','+str(spectacol[3])+'\n'
                file.write(txt)
