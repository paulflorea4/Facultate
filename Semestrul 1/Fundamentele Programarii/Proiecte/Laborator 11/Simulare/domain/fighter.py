class Fighter:
    def __init__(self,id:int,unit_type:str,health_points:int,attack_points:int,army:str):
        self.__id=id
        self.__unit_type=unit_type
        self.__health_points=health_points
        self.__attack_points=attack_points
        self.__army=army

    def get_id(self):
        """
        Returneaza id-ul luptatorului
        :return:
        """
        return self.__id

    def get_unit_type(self):
        """
        Returneaza tipul luptatorului
        :return:
        """
        return self.__unit_type

    def get_health_points(self):
        """
        Returneaza viata luptatorului
        :return:
        """
        return self.__health_points

    def set_health_points(self,health_points):
        """
        Schimba valoarea vietii luptatorului
        :param health_points: noua valoare
        :return: -
        """
        self.__health_points=health_points

    def get_attack_points(self):
        """
        Returneaza punctele de atac ale luptatorului
        :return:
        """
        return self.__attack_points

    def get_army(self):
        """
        Returneaza armata luptatorului
        :return:
        """
        return self.__army

    def __str__(self):
        return (str(self.__id)+','+self.__unit_type+','+str(self.__health_points)+','+str(self.__attack_points)+
                ','+self.__army)

    def __eq__(self,other):
        if other.get_id() == self.get_id():
            return True
        return False

