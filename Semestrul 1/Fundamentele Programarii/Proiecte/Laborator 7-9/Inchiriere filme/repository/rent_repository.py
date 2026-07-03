from domain.client import Client
from domain.movie import Movie
from domain.rent import Rent

class RentRepository:
    def __init__(self):
        self.__rents=[]

    def find(self,rent):
        """
        Cauta o inchiriere in lista de inchirieri
        :param rent:inchirierea de cautat
        :return:True daca o inchiriere facuta de aceeasi persoana si pentru acelasi film ca si in cel dat ca parametru
                este in lista
                False,altfel
        """
        for existing_rent in self.__rents:
            if rent==existing_rent:
                return True
        return None

    def store(self,rent:Rent):
        """
        Adauga o inchiriere in lista de inchirieri
        :param rent:inchirierea de adaugat
        :return:-;
        :raises:ValueError daca exista deja o inchiriere pentru filmul si clientul dat
        """
        if self.find(rent):
            raise ValueError("Exista deja o rezervare pentru filmul si persoana data")
        self.__rents.append(rent)

    def __find_pos(self,rent:Rent):
        """
        Gaseste pozitia in lista a inchirierii data ca parametru
        :param rent: inchirierea de cautat
        :return:pozitia din lista a inchirierii daca exista,-1 in caz contrar
        """
        pos=-1
        for index,existing_rent in enumerate(self.__rents):
            if existing_rent==rent:
                pos=index
                break
        return pos

    def delete(self,rent:Rent):
        """
        Sterge inchiererea data ca parametru
        :param rent:inchirierea de sters
        :return:-
        :raises:ValueError daca nu exista inchiriearea pentru filmul si clientul dat
        """
        pos=self.__find_pos(rent)
        if pos==-1:
            raise ValueError("Nu exista inchirierea pentru filmul si clientul dat")
        self.__rents.pop(pos)

    def get_all(self):
        """
        Returneaza lista de inchirieri
        :return:
        """
        return self.__rents

    def get_size(self):
        return len(self.__rents)

class RentFileRepository(RentRepository):
    def __init__(self,filename):
        super().__init__()
        self.__filename=filename
        self.__load_from_file()

    def __load_from_file(self):
        """
        Incarca datele din fisier
        :return:-
        :raises:ValueError daca exista probleme la citirea datelor din fiiser
        """
        try:
            with open(self.__filename,'r') as file:
                lines=file.readlines()
                for line in lines:
                    line=line.strip()
                    if line:
                        client_id,client_name,client_cnp,movie_id,movie_title,movie_description,movie_type=line.split(",")
                        movie=Movie(int(movie_id),movie_title,movie_description,movie_type)
                        client=Client(int(client_id),client_name,client_cnp)
                        rent=Rent(client,movie)
                        super().store(rent)
        except IOError:
            raise ValueError("Nu s-au putut citi datele din fisierul"+self.__filename)

    def store(self,rent:Rent):
        super().store(rent)
        self.__save_to_file()

    def delete(self,rent:Rent):
        super().delete(rent)
        self.__save_to_file()

    def __save_to_file(self):
        """
        Salveaza datele in fisier
        :return: -
        :raises:ValueError daca exista probleme la scrierea in fisier
        """
        try:
            with (open(self.__filename,'w') as file):
                rents=super().get_all()
                for rent in rents:
                    rent_str=str(rent.client.get_id())+','+rent.client.name+','+rent.client.cnp+','+str(rent.movie.get_id()
                                    )+','+rent.movie.title+','+rent.movie.description+','+rent.movie.type
                    rent_str+='\n'
                    file.write(rent_str)
        except IOError:
            raise ValueError("Nu s-au putut salva datele in fisierul"+self.__filename)