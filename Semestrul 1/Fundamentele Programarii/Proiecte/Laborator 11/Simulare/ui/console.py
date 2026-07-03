from service.FigherController import FigherController

class Console:
    def __init__(self,service: FigherController):
        self.service = service

    @staticmethod
    def print_menu():
        print("1.Functionalitate 1")
        print("2.Functionalitate 2")
        print("E.Iesire din aplicatie")

    def first_task_ui(self):
        ordered_list=self.service.first_task()
        for army_name,army_fighters in ordered_list:
            print(army_name,end=":")
            for fighter_unit,fighter_health in army_fighters:
                print(fighter_unit+'('+str(fighter_health)+') ',end="")
            print()

    def second_task_ui(self):
        id1=input("Introduceti primul id:")
        id2=input("Introduceti al doilea id:")
        try:
            id1=int(id1)
        except ValueError:
            print("Id-ul trebuie sa fie numar intreg")
            return

        try:
            id2=int(id2)
        except ValueError:
            print("Id-ul trebuie sa fie numar intreg")
            return

        result=self.service.second_task(id1,id2)
        print(result)

    def run(self):
        while True:
            self.print_menu()
            user_input = input(">>>").upper().strip()
            match user_input:
                case "1":
                    self.first_task_ui()
                case "2":
                    self.second_task_ui()
                case "E":
                    break
                case _:
                    print("Optiune invalida")