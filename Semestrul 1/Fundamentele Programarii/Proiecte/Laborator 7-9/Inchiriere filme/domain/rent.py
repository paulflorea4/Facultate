class Rent:
    def __init__(self, client,movie):
        self.__client=client
        self.__movie=movie

    @property
    def client(self):
        return self.__client

    @property
    def movie(self):
        return self.__movie

    @movie.setter
    def movie(self,new_movie):
        self.__movie=new_movie

    def __eq__(self,other):
        if type(self) != type(other):
            return False
        return self.__client == other.__client and self.__movie == other.__movie

    def __str__(self):
        return "[Inchiriere]: "+str(self.__movie)+" | "+str(self.__client)