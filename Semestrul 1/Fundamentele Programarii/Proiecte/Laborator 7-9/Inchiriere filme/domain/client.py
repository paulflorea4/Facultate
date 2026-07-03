class Client:
    def __init__(self,id:int,name:str,cnp:str):
        self.__id = id
        self.__name = name
        self.__CNP = cnp

    def get_id(self):
        return self.__id

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self,new_name:str):
        if not isinstance(new_name,str):
            raise ValueError('Numele trebuie sa fie de tip string')
        self.__name = new_name

    @property
    def cnp(self):
        return self.__CNP

    @cnp.setter
    def cnp(self,new_cnp:str):
        if not isinstance(new_cnp,str):
            raise ValueError('CNP-ul trebuie sa fie de tip string')
        self.__CNP = new_cnp

    def __str__(self):
        return "[" + str(self.__id)+ "] Client: Nume = " + self.__name+ ", CNP = " + self.__CNP

