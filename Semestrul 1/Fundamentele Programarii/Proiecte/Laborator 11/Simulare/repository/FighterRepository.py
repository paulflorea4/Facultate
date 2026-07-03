from domain.fighter import Fighter

class FighterRepository:
    def __init__(self,filename):
        self.__elements=[]
        self.__filename=filename
        self.__load_from_file()

    def find(self,id:int):
        """
        Cauta in lista de luptatori id-ul dat
        :param id: id-ul de cautat (numar intreg)
        :return: luptatorul respectiv daca exista,None altfel
        """
        for existing_fighter in self.__elements:
            if existing_fighter.get_id() == id:
                return existing_fighter
        return None

    def store(self,fighter:Fighter):
        """
        Adauga in lista de luptatori luptatorul dat daca acesta nu exista
        :param fighter: obiect de tip Fighter
        :return: -
        :raises:ValueError daca luptatoru cu id-ul dat exista deja in lista
        """
        if self.find(fighter.get_id()) is not None:
            raise ValueError("Luptatorul cu id-ul dat exista deja")
        self.__elements.append(fighter)
        self.__save_to_file()

    def __load_from_file(self):
        try:
            with open(self.__filename,'r') as file:
                lines=file.readlines()
                for line in lines:
                    line=line.strip()
                    if line:
                        id,unit_type,health_points,attack_points,army=line.split(",")
                        fighter=Fighter(int(id),unit_type,int(health_points),int(attack_points),army)
                        self.store(fighter)
        except IOError:
            print("Nu s-au putut citi datele din fisier")

    def __save_to_file(self):
        try:
            with open(self.__filename,'w') as file:
                for fighter in self.__elements:
                    fighter_string=str(fighter)+'\n'
                    file.write(fighter_string)
        except IOError:
            print("Nu s-au putut salva datele in fisier")

    def get_all(self):
        """
        Returneaza luptatorii din lista
        :return:
        """
        return self.__elements

    def clear_file(self):
        """
        Sterge continutul din fisier
        :return:
        """
        with open(self.__filename,'w') as file:
            pass