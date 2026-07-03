from domain.movie import Movie

class MovieRepository:
    def __init__(self):
        self.__elements=[]

    def find(self, id: int, index: int = 0):
        """
        Cauta un film dupa id dat(RECURSIV)
        :param id: id-ul cautat
        :param index: indexul curent pentru recursivitate (implicit 0)
        :return: obiect de tip Movie daca exista film cu id-ul dat, None altfel
        """
        if index >= len(self.__elements):
            return None

        movie = self.__elements[index]
        if movie.get_id() == id:
            return movie

        return self.find(id, index + 1)

    def store(self,movie:Movie):
        """
        Adauga un film in lista de filme
        :param movie:filmul de adaugat
        :return:-;
        :raises:ValueError daca se incearca adaugarea unui film cu id care exista deja
        """
        if self.find(movie.get_id()) is not None:
            raise ValueError("Exista deja un film cu acest id")
        self.__elements.append(movie)

    def __find_pos(self,id:int):
        """
        Gaseste pozitia in lista a filmului cu id dat (daca exista)
        :param id:id-ul cautat
        :return:pozitia din lista a filmului cu id dat,pos returnat intre 0 si len(self.__elements) daca filmul exista,
                -1 in caz contrar
        """
        pos=-1
        for index,movie in enumerate(self.__elements):
            if movie.get_id()==id:
                pos=index
                break
        return pos

    def update(self,updated_movie:Movie):
        """
        Actualizeaza filmul din lista cu ID=id-ul filmului dat ca parametru
        :param updated_movie:filmul actualizat
        :return:-
        :raises:ValueError daca nu exista film cu id-ul filmului actualizat
        """
        pos = self.__find_pos(updated_movie.get_id())
        if pos==-1:
            raise ValueError("Nu exista film cu id-ul dat")
        self.__elements[pos] = updated_movie

    def delete(self,id:int):
        """
        Sterge filmul cu id-ul dat
        :param id: id-ul de sters
        :return: -
        :raises: ValueError daca nu exista film cu id-ul dat
        """
        pos = self.__find_pos(id)
        if pos == -1:
            raise ValueError("Nu exista film cu id-ul dat")
        self.__elements.pop(pos)

    def get_all(self):
        return self.__elements

    def get_size(self):
        return len(self.__elements)

class MovieFileRepository(MovieRepository):
    def __init__(self,filename):
        super().__init__()
        self.__filename=filename
        self.__load_from_file()

    def __load_from_file(self):
        """
        Incarca datele din fisier
        :return:-
        :raises:ValueError daca exista probleme la citirea datelor din fisier
        """
        try:
            with open(self.__filename,'r') as file:
                lines=file.readlines()
                for line in lines:
                    line=line.strip()
                    if line:
                        movie_id,title,description,type=line.split(',')
                        movie=Movie(int(movie_id),title,description,type)
                        super().store(movie)
        except IOError:
            raise ValueError("Nu s-au putut citi datele din fisierul"+self.__filename)

    def store(self,movie:Movie):
        super().store(movie)
        self.__save_to_file()

    def delete(self,id:int):
        super().delete(id)
        self.__save_to_file()

    def update(self,updated_movie:Movie):
        super().update(updated_movie)
        self.__save_to_file()

    def __save_to_file(self):
        """
        Salveaza datele in fisier
        :return: -
        :raises:ValueError daca exista probleme la scrierea in fisier
        """
        try:
            with open(self.__filename,'w') as file:
                movies=super().get_all()
                for movie in movies:
                    movie_str=str(movie.get_id())+','+movie.title+','+movie.description+','+movie.type
                    movie_str+="\n"
                    file.write(movie_str)
        except IOError:
            raise ValueError("Nu s-au putut salva datele in fisierul"+self.__filename)

