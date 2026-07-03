from domain.produs import Produs


class RepoProduse:
    def __init__(self,filename):
        self.__filename = filename
        self.__produse=[]
        self.__undo_list=[]
        self.__load_from_file()

    def __load_from_file(self):
        try:
            with open(self.__filename,'r') as file:
                for line in file:
                    line=line.strip()
                    if line:
                        id,denumire,pret=line.split(",")
                        produs=Produs(id,denumire,int(pret))
                        self.__produse.append(produs)
        except IOError:
            print("Nu s-au putut citi datele din fisierul: "+self.__filename)

    def write_to_file(self):
        with open(self.__filename,'w') as file:
            for produs in self.__produse:
                file.write(produs.__str__()+'\n')

    def find(self,id:str):
        for produs in self.__produse:
            if produs.id() == id:
                return produs
        return None

    def store(self,produs):
        if self.find(produs.id) is not None:
            raise ValueError("Exista deja un produs cu acest id")
        self.__produse.append(produs)
        self.write_to_file()

    def delete(self,id):
        produs=self.find(id)
        if produs is None:
            raise ValueError("Nu exista produsul cu acest id")
        self.__undo_list.append(self.__produse.copy())
        self.__produse.remove(produs)
        self.write_to_file()

    def undo(self):
        if self.__undo_list == []:
            raise ValueError("Nu se mai poate face undo")
        self.__produse = self.__undo_list.pop()
        self.write_to_file()

    def get_produse(self):
        return self.__produse

