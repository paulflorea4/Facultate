class Movie:
    def __init__(self, id:int,title:str,description:str,type:str):
        self.__id = id
        self.__title = title
        self.__description = description
        self.__type = type

    def get_id(self):
        return self.__id

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self,new_title:str):
        if not isinstance(new_title,str):
            raise ValueError('Titlul poate fi doar de tip string.')
        self.__title = new_title

    @property
    def description(self):
        return self.__description

    @description.setter
    def description(self,new_description:str):
        if not isinstance(new_description,str):
            raise ValueError('Descrierea poate fi doar de tip string.')
        self.__description = new_description

    @property
    def type(self):
        return self.__type

    @type.setter
    def type(self,new_type:str):
        if not isinstance(new_type,str):
            raise ValueError('Genul poate fi doar de tip string.')
        self.__type = new_type

    def __str__(self):
        return "[" + str(self.__id) + "] Film: Titlu = " + self.__title + ", Descriere = " + self.__description + ", Gen = " + self.__type

