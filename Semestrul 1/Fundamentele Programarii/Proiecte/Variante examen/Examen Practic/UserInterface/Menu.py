class Menu:
    def __init__(self, service):
        self.__service = service

    def print_menu(self):
        print("1.Adauga automobil")
        print("2.Sterge automobil")
        print("3.Filtrare automobil")
        print("4.Undo")
        print("5.Iesire")

    def handle_input(self, input):
        try:
            if input == "5":
                return False
            if input == "1":
                self.__add_vehicle()
            elif input == "2":
                self.__delete_vehicle()
            elif input == "3":
                self.__filter_vehicle()
            elif input == "4":
                self.__undo()
            else:
                raise Exception("Optiune invalida!")
        except Exception as e:
            print(e)

    def __add_vehicle(self):
        id = input("Introduceti id-ul: ")
        marca = input("Introduceti marca: ")
        pret = input("Introduceti pret: ")
        model = input("Introduceti model: ")
        data = input("Introduceti data (zi:luna:an): ")
        try:
            self.__service.add_vehicle(id, marca, pret, model, data)
            print("Vehiculul a fost adaugat cu succes!")
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            print(e)

    def __delete_vehicle(self):
        digit = input("Introduceti o cifra: ")
        try:
            number_of_deleted_vehicles = self.__service.delete_vehicle(digit)
            print(f"Au fost sterse {number_of_deleted_vehicles} automobile!")
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            print(e)

    def __filter_vehicle(self):
        marca_filter = input("Introduceti filtru pentru marca: ")
        pret_filter = input("Introduceti filtru pentru pret: ")
        try:
            self.__service.filter_vehicles(marca_filter, pret_filter)
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            raise e

    def __undo(self):
        try:
            self.__service.undo()
        except Exception as e:
            print(e)

    def print_dictionary(self, dictionary):
        if "marca_filter" in dictionary:
            print("Filtru marca: " + dictionary["marca_filter"])
        if "pret_filter" in dictionary:
            print("Filtru pret: " + str((dictionary["pret_filter"])))
        for key, value in dictionary.items():
            if key != "marca_filter" and key != "pret_filter":
                print(f"{value}")

    def run(self):
        """
        Functia principala a clasei Menu care dirijeaza executia acesteia
        """
        exit_requested = False
        while not exit_requested:
            self.print_menu()
            if self.handle_input(input()) == False:
                exit_requested = True
        print("La revedere!")