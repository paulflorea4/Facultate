import datetime


class Vehicle:
    def __init__(self, id, marca, pret, model, data):
        self.__id = id
        self.__marca = marca
        self.__pret = pret
        self.__model = model
        self.__data = data

    def get_id(self):
        return self.__id

    def get_marca(self):
        return self.__marca

    def get_pret(self):
        return self.__pret

    def get_model(self):
        return self.__model

    def get_data(self):
        return self.__data

    def set_id(self, id):
        self.__id = id

    def set_marca(self, marca):
        self.__marca = marca

    def set_pret(self, pret):
        self.__pret = pret

    def set_model(self, model):
        self.__model = model

    def set_data(self, data):
        self.__data = data

    def __str__(self):
        return f"{self.get_id()}, {self.get_marca()}, {self.get_pret()}, {self.get_model()}, {self.get_data().strftime('%d:%m:%Y')}"

class VehicleValidator:
    def validate_id(self, id):
        try:
            id = int(id)
            if id < 0:
                return False
            return True
        except ValueError:
            return False

    def validate_marca(self, marca):
        if not isinstance(marca, str):
            return False
        return marca != ""

    def validate_pret(self, pret):
        try:
            pret = float(pret)
            if pret <= 0:
                return False
            return True
        except ValueError:
            return False

    def validate_model(self, model):
        if not isinstance(model, str):
            return False
        return model != ""

    def validate_data(self, data):
        data = data.split(":")
        try:
            datetime.datetime(int(data[2]), int(data[1]), int(data[0]))
            return True
        except Exception:
            return False
