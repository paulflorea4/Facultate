import datetime

from Domain.Vehicle import Vehicle


class MemoryRepository:
    """
    Clasa defineste repository-ul ce stocheaza datele in memorie al aplicatiei
    """

    def __init__(self):
        """
        Constructorul clasei MemoryRepository
        """
        self._vehicles = {}

    def add_vehicle(self, vehicle):
        """
        Functia realizeaza operatia de adaugare a unui automobil in lista de
        automobile
        :param vehicle: vehiculul adaugat (Vehicle)
        :raises: Exception (daca un vehicul cu acelasi id exista deja)
        """
        if vehicle.get_id() in self._vehicles:
            raise Exception("Un automobil cu acest id exista deja!")
        else:
            self._vehicles[vehicle.get_id()] = vehicle

    def remove_vehicle_by_id(self, id):
        """
        Functia realizeaza operatia de stergere a unui automobil din lista de
        automobile dupa id
        :param id: id-ul automobilului (Integer)
        :raises: Exception (daca un vehicul cu acest id nu exista)
        """
        if id not in self._vehicles:
            raise Exception("Un automobil cu acest id nu exista!")
        else:
            del self._vehicles[id]

    def get_vehicle_by_id(self, id):
        """
        Functia returneaza automobilul cu id-ul transmis ca parametru
        :param id: id-ul automobilului (Integer)
        :return: automobilul (Vehicle)
        :raises: Exception (daca un vehicul cu acest id nu exista)
        """
        if id not in self._vehicles:
            raise Exception("Un automobil cu acest id nu exista!")
        else:
            return self._vehicles[id]

    def get_all_vehicles(self):
        """
        Functia returneaza lista de automobile
        :return: lista de automobile (Dictionary of Vehicle)
        """
        return self._vehicles

    def set_vehicles(self, vehicles):
        """
        Setter-ul listei de vehicule
        :param vehicles: lista de vehicule (Dictionary of Vehicle)
        """
        self._vehicles = vehicles

class FileRepository(MemoryRepository):
    """
    Clasa defineste repository-ul ce stocheaza datele in fisier al aplicatiei
    """

    def __init__(self, file_name):
        """
        Constructorul clasei MemoryRepository
        :param file_name: fisierul din care se preiau si se stocheaza date (String)
        """
        super().__init__()
        self.__file_name = file_name
        self.__read_from_file()

    def __read_from_file(self):
        """
        Functia realizezaza operatia de stocare a vehiculelor in fisier
        """
        self._vehicles = {}
        with open(self.__file_name, mode='r') as file:
            for line in file.readlines():
                data = line.split(',')
                id = int(data[0].strip())
                marca = data[1].strip()
                pret = float(data[2].strip())
                model = data[3].strip()
                data[4] = data[4].strip().split(':')
                data_calendar = datetime.datetime(int(data[4][2]), int(data[4][1]), int(data[4][0]))
                super().add_vehicle(Vehicle(id, marca, pret, model, data_calendar))

    def __write_to_file(self):
        """
        Functia realizezaza operatia de stocare a vehiculelor in fisier
        """
        with open(self.__file_name, mode='w') as file:
            vehicles = super().get_all_vehicles()
            for key, value in vehicles.items():
                file.write(str(value) + "\n")

    def add_vehicle(self, vehicle):
        """
        Functia realizeaza operatia de adaugare a unui automobil in lista de
        automobile si actualizeaza fisierul in mod corespunzator
        :param vehicle: vehiculul adaugat (Vehicle)
        :raises: Exception (daca un vehicul cu acelasi id exista deja)
        """
        self.__read_from_file()
        super().add_vehicle(vehicle)
        self.__write_to_file()

    def remove_vehicle_by_id(self, id):
        """
        Functia realizeaza operatia de stergere a unui automobil din lista de
        automobile dupa id si actualizeaza fisierul in mod corespunzator
        :param id: id-ul automobilului (Integer)
        :raises: Exception (daca un vehicul cu acest id nu exista)
        """
        self.__read_from_file()
        super().remove_vehicle_by_id(id)
        self.__write_to_file()

    def get_vehicle_by_id(self, id):
        """
        Functia returneaza automobilul cu id-ul transmis ca parametru
        din fisier
        :param id: id-ul automobilului (Integer)
        :return: automobilul (Vehicle)
        :raises: Exception (daca un vehicul cu acest id nu exista)
        """
        self.__read_from_file()
        return super().get_vehicle_by_id(id)

    def get_all_vehicles(self):
        """
        Functia returneaza lista de automobile din fisier
        :return: lista de automobile (Dictionary of Vehicle)
        """
        self.__read_from_file()
        return super().get_all_vehicles()

    def set_vehicles(self, vehicles):
        """
        Setter-ul listei de vehicule
        :param vehicles: lista de vehicule (Dictionary of Vehicle)
        """
        super().set_vehicles(vehicles)
        self.__write_to_file()
