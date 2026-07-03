from Domain.Vehicle import VehicleValidator, Vehicle
import datetime

class Service:
    """
    Clasa defineste service-ul repository-ului de automobile
    """

    def __init__(self, repository):
        """
        Constructorul clasei Service
        :param repository: repository-ul folosit de service (VehicleRepository)
        """
        self.__repository = repository
        self.__validator = VehicleValidator()
        self.__marca_filter = ""
        self.__pret_filter = -1
        self.__filtered_vehicles = self.__repository.get_all_vehicles()
        self.__history_vehicles = []

    def add_vehicle(self, id, marca, pret, model, data):
        """
        Functia realizeaza prin repository adaugarea unui automobil in lista de automobile
        :param id: id-ul automobilului (Integer >= 0)
        :param marca: marca automobilului (String)
        :param pret: pretul automobilului (Float > 0)
        :param model: modelul automobilului (String)
        :param data: data de expirare itp (String)
        :raises: Exception (operatia nu s-a efectuat cu succes)
        """
        if self.__validator.validate_id(id) == False:
            raise Exception("Id-ul trebuie sa fie un numar natural!")
        if self.__validator.validate_marca(marca) == False:
            raise Exception("Marca nu poate fi vida!")
        if self.__validator.validate_pret(pret) == False:
            raise Exception("Pretul trebuie sa fie un numar real > 0!")
        if self.__validator.validate_model(model) == False:
            raise Exception("Modelul nu poate fi vid!")
        if self.__validator.validate_data(data) == False:
            raise Exception("Data trebuie sa fie valida si sa respecte formatul zi:luna:an!")
        id = int(id)
        pret = float(pret)
        data = data.split(":")
        data = datetime.datetime(int(data[2]), int(data[1]), int(data[0]))
        self.__history_vehicles.append(dict(self.__repository.get_all_vehicles()))
        self.__repository.add_vehicle(Vehicle(id, marca, pret, model, data))

    def delete_vehicle(self, digit):
        """
        Functia realizeaza prin repository stergerea automobilelor ce contin
        in id cifra transmisa ca parametru
        :param digit: cifra (Integer >= 0 si <= 9)
        :return: numarul de vehicule sterse (Integer)
        :raises: Exception (operatia nu s-a efectuat cu succes)
        """
        if self.__validator.validate_id(digit) == False:
            raise Exception("Cifra trebuie sa fie un numar natural >= 0 si <= 9!")
        digit = int(digit)
        if digit < 0 or digit > 9:
            raise Exception("Cifra trebuie sa fie un numar natural >= 0 si <= 9!")
        vehicles = self.__repository.get_all_vehicles()
        self.__history_vehicles.append(dict(vehicles))
        number_of_deleted_vehicles = 0
        for key, value in vehicles.items():
            if str(digit) in str(key):
                self.__repository.remove_vehicle_by_id(key)
                number_of_deleted_vehicles += 1
        return number_of_deleted_vehicles

    def filter_vehicles(self, marca_filter, pret_filter):
        """
        Functia realizeaza operatia de filtrare a listei de automobile
        :param marca_filter: filtru pentru marca (String)
        :param pret_filter: filtru pentru pret (Float > 0)
        :raises: Exception (operatia nu s-a efectuat cu succes)
        """
        if self.__validator.validate_pret(pret_filter) == False and pret_filter != "-1" and pret_filter != -1:
            raise Exception("Pret trebuie sa fie un numar real > 0!")
        self.__marca_filter = marca_filter
        self.__pret_filter = float(pret_filter)

        vehicles = self.__repository.get_all_vehicles()
        first_filter = {}
        second_filter = {}

        if self.__marca_filter != "":
            for key, value in vehicles.items():
                if self.__marca_filter in str(value.get_marca()):
                    first_filter[key] = value
        else:
            first_filter = vehicles

        if self.__pret_filter != -1:
            for key, value in first_filter.items():
                if self.__pret_filter > value.get_pret():
                    second_filter[key] = value
        else:
            second_filter = first_filter

        for key, value in second_filter.items():
            if datetime.datetime.today() > value.get_data():
                second_filter[key].set_marca("*" + second_filter[key].get_marca())

        self.__filtered_vehicles = second_filter


    def get_filtered_vehicles(self):
        """
        Functia returneaza lista de automobile cu filtrele aplicate (si filtrele)
        :return: lista de automobile (Dictionary of Vehicle)
        """
        self.filter_vehicles(self.__marca_filter, self.__pret_filter)
        result = dict(self.__filtered_vehicles)
        result["marca_filter"] = self.__marca_filter
        result["pret_filter"] = self.__pret_filter
        return result

    def undo(self):
        """
        Functia realizeaza operatia de undo a ultimei operatii de adaugare / stergere
        :raises: Exception (daca nu mai exista operatii la care sa se faca undo)
        """
        if len(self.__history_vehicles) <= 0:
            raise Exception("Nu se mai poate realiza undo!")
        self.__repository.set_vehicles(self.__history_vehicles.pop())
