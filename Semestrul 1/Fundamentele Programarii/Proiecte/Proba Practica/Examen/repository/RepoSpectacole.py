from domain.spectacol import Spectacol

class RepoSpectacole:
    def __init__(self,filename):
        self.__filename = filename
        self.__spectacole=[]
        self.__load_from_file()

    def __load_from_file(self):
        """
        Incarca datele din fisier in lista de spectacole
        :return: -
        :raises: IOError daca fisierul nu s-a putut deschide
        """
        with open(self.__filename,'r') as file:
            lines=file.readlines()
            for line in lines:
                line=line.strip()
                data=line.split(",")
                titlu,artist,gen,durata=data[0],data[1],data[2],int(data[3])
                self.store(Spectacol(titlu,artist,gen,durata))

    def find(self,titlu,artist):
        """
        Cauta spectacolul cu titlul si artistul dat ca parametru
        :param titlu: string care reprezinta titlul spectacolului
        :param artist: string care reprezinta artistul spectacolului
        :return: spectacolul cu titlul si artistul dat daca exista,None altfel
        """
        for spectacol_existent in self.__spectacole:
            if spectacol_existent.titlu() == titlu and spectacol_existent.artist() == artist:
                return spectacol_existent
        return None

    def store(self,spectacol:Spectacol):
        """
        Adauga la lista de spectacole obiectul de tip spectacol dat ca parametru daca acesta nu exista
        :param spectacol: obiect de tip spectacol de adaugat
        :return: -
        :raises: ValueError daca mai exita un spectacol cu titlul si artisul dat
        """
        if self.find(spectacol.titlu(),spectacol.artist()) is not None:
            raise ValueError("Exista deja un spectacol cu titlul si artistul dat")
        self.__spectacole.append(spectacol)
        self.__write_to_file()

    def modify(self,spectacol:Spectacol):
        """
        Modifica spectacolul din lista care are acelasi titlu si artist
        :param spectacol: obiectul de tip spectacol de modificat
        :return: -
        :raises: ValueError daca nu exista spectacolul cu titlul si artistul dat
        """
        spectacol_cautat=self.find(spectacol.titlu(),spectacol.artist())
        if spectacol_cautat is not None:
            self.__spectacole.remove(spectacol_cautat)
            self.store(spectacol)
            self.__write_to_file()
        else:
            raise ValueError("Nu exista un spectacol cu titlul si artistul dat")

    def __write_to_file(self):
        """
        Scrie spectacolele din lista in fisier
        :return: -
        """
        with open(self.__filename,'w') as file:
            for spectacol in self.__spectacole:
                file.write(str(spectacol)+'\n')

    def get_len(self):
        """
        Returneaza lungimea listei de spectacole
        :return:
        """
        return len(self.__spectacole)

    def get_all(self):
        """
        Returneaza lista de spectacole
        :return:
        """
        return self.__spectacole

    def empty_file(self):
        """
        Sterge continutul fisierului dat
        :return:
        """
        with open(self.__filename,'w') as file:
            file.write("")

