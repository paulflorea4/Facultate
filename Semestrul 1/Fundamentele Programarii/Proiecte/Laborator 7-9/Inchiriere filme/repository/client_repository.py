from domain.client import Client

class ClientRepository:
    def __init__(self):
        self.__elements=[]

    def find(self,id:int,index:int=0):
        """
        Cauta clientul cu id dat(RECURSIV)
        :param index:index-ul de la care se incepe cautarea
        :param id:id-ul cautat
        :return:obiect de tip Client daca exista,None altfel
        """
        if index >= len(self.__elements):
            return None

        client = self.__elements[index]
        if client.get_id() == id:
            return client

        return self.find(id, index + 1)

    def store(self,client:Client):
        """
        Adauga un client la lista de clienti
        :param client:clientul de adaugat
        :return:-;
        :raises: ValueError daca se incearca adaugarea unui client cu id care exista deja
        """
        if self.find(client.get_id()) is not None:
            raise ValueError("Exista deja un client cu acest id")
        self.__elements.append(client)

    def __find_pos(self,id:int):
        """
        Gaseste pozitia in lista a clientului cu id dat(daca exista)
        :param id:id-ul cautat
        :return:pozitia in lista a clientului cu id dat,pos returnat intre 0 si len(self.__elements) daca clientul exista,
                -1 in caz contrar

        Complexitate:O(n),unde n e numarul de elemente al listei
        Best Case:O(1) cand id ul cautat este pe prima pozitie
        Worst Case:O(n) cand id ul cautat este pe ultima pozitie
        Average Case:1/n+2/n+3/n+...+n/n=n(n+1)/2n=(n+1)/2 -> O(n)
        """
        pos=-1
        for index,client in enumerate(self.__elements):
            if client.get_id() == id:
                pos=index
                break
        return pos

    def update(self,updated_client:Client):
        """
        Actualizeaza clientul din lista cu ID=id-ul clientului dat ca parametru
        :param updated_client:clientul actualizat
        :return:-
        :raises:ValueError daca nu exista client cu id-ul clientului actualizat
        """
        pos=self.__find_pos(updated_client.get_id())
        if pos == -1:
            raise ValueError("Nu exista client cu id-ul dat")
        self.__elements[pos] = updated_client

    def delete(self,id:int):
        """
        Sterge clientul cu id-ul dat,daca exista
        :param id: id-ul de sters
        :return: -
        :raises:ValueError daca nu exista client cu id-ul dat
        """
        pos=self.__find_pos(id)
        if pos == -1:
            raise ValueError("Nu exista client cu id-ul dat")
        self.__elements.pop(pos)

    def get_all(self):
        return self.__elements

    def get_size(self):
        return len(self.__elements)

class ClientFileRepository(ClientRepository):
    def __init__(self,filename):
        super().__init__()
        self.__filename=filename
        self.__load_from_file()

    def __load_from_file(self):
        """
        Incarca datele din fisier
        :return: -
        :raises:ValueError daca exista probleme la citirea datelor din fisier
        """
        try:
            with open(self.__filename,'r') as file:
                lines=file.readlines()
                for line in lines:
                    line=line.strip()
                    if line:
                        client_id,client_name,client_cnp=line.split(',')
                        client=Client(int(client_id),client_name.strip(),client_cnp.strip())
                        super().store(client)
        except IOError:
            raise ValueError("Nu s-au putut citi datele din fisierul" + self.__filename)

    def store(self,client:Client):
        super().store(client)
        self.__save_to_file()

    def delete(self,id:int):
        super().delete(id)
        self.__save_to_file()

    def update(self,updated_client:Client):
        super().update(updated_client)
        self.__save_to_file()

    def __save_to_file(self):
        """
        Salveaza datele in fisier
        :return: -
        :raises:ValueError daca exista probleme la scrierea in fisier
        """
        try:
            with open(self.__filename,'w') as file:
                clients=super().get_all()
                for client in clients:
                    client_str=str(client.get_id())+','+str(client.name)+','+str(client.cnp)
                    client_str+='\n'
                    file.write(client_str)

        except IOError:
            raise ValueError("Nu s-au putut salva datele in fisierul"+self.__filename)