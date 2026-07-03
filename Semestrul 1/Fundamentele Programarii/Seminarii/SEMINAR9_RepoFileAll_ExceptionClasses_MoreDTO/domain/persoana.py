class Persoana:
    def __init__(self, cnp: str, name: str):
        self.__cnp = cnp
        self.__name = name

    @property
    def cnp(self):
        return self.__cnp

    @property
    def nume(self):
        return self.__name


    @nume.setter
    def nume(self, new_name):
        self.__name = new_name



    def __eq__(self, other):
        if type(self) != type(other):
            return False
        return self.__cnp == other.__cnp

    def __str__(self):
        return "[" + str(
            self.__cnp) + "] Persoana: Nume = " + self.__name
