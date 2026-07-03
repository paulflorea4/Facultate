from domain.melodie import Melodie


class RepoMelodii:
    def __init__(self,filename):
        self.__filename = filename

    def find(self,titlu,artist):
        melodii=self.__load_from_file()
        for melodie in melodii:
            if melodie.get_titlu()==titlu and melodie.get_artist()==artist:
                return melodie
        return None

    def __load_from_file(self):
        try:
            melodii=[]
            with open(self.__filename,'r') as f:
                lines=f.readlines()
                for line in lines:
                    line=line.strip()
                    if line:
                        titlu,artist,gen,durata=line.split(",")
                        melodie=Melodie(titlu,artist,gen,int(durata))
                        melodii.append(melodie)
                return melodii
        except IOError:
            raise ValueError("Nu s-a putut citi din fisierul: "+self.__filename)

