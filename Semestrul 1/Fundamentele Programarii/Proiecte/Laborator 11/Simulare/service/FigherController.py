from repository.FighterRepository import FighterRepository
import random

class FigherController:
    def __init__(self,repository: FighterRepository):
        self.repository = repository

    def first_task(self):
        """
        Functia returneaza pentru fiecare armata existenta tipurile unice de untiati existente impreuna cu valoarea
        medie de health points pe unitate,sortate descrescator dupa valoarea medie de health points pe unitate
        :return: lista ordonata
        """
        fighters=self.repository.get_all()
        army={}
        for fighter in fighters:
            if fighter.get_army() in army:
                if fighter.get_unit_type() in army[fighter.get_army()]:
                    army[fighter.get_army()][fighter.get_unit_type()].append(fighter.get_health_points())
                else:
                    army[fighter.get_army()][fighter.get_unit_type()] = [fighter.get_health_points()]
            else:
                army[fighter.get_army()]={}
                army[fighter.get_army()][fighter.get_unit_type()] = [fighter.get_health_points()]

        for army_key in army:
            for unit in army[army_key]:
                army[army_key][unit] = sum(army[army_key][unit])/len(army[army_key][unit])

        ordered_list=[]
        for army_key,army_value in army.items():
            sorted_army=sorted(army_value.items(), key=lambda x: x[1], reverse=True)
            ordered_list.append((army_key,sorted_army))

        return ordered_list

    def second_task(self,id1:int,id2:int):
        """
        Functia returneaza castigatorul dintre lupta a doua unitati cu 2 id-uri date;daca acestea sunt
        din aceeasi armata se va afisa mesajul corespunzator
        :param id1: id-ul primei unitati
        :param id2: id-ul celeilalte unitati
        :return: result-mesaj cu armata castigatoare daca,"draw"-daca este egalitate sau daca unitatile sunt din aceeasi
                        armata
        """
        random.seed(42)
        fighter1=self.repository.find(id1)
        fighter2=self.repository.find(id2)
        if fighter1.get_army()==fighter2.get_army():
            result="Unitatile sunt din aceeasi armata"
            return result
        attack=0
        fighter1_health_points=fighter1.get_health_points()
        fighter2_health_points=fighter2.get_health_points()
        while fighter1.get_health_points()>0 and fighter2.get_health_points()>0:
            if attack==0:
                random_damage = random.randint(0, fighter2.get_attack_points())
                new_health_point=fighter2.get_health_points()-random_damage
                fighter2.set_health_points(new_health_point)
                attack=1
            else:
                random_damage = random.randint(0, fighter1.get_attack_points())
                new_health_point = fighter1.get_health_points() - random_damage
                fighter1.set_health_points(new_health_point)
                attack=0
            #print(f"{fighter1.get_health_points()},{fighter2.get_health_points()}")

        result="draw"
        if fighter1.get_health_points()>0 and fighter2.get_health_points()<=0:
            result=f"{fighter1.get_army()} wins"

        if fighter2.get_health_points()>0 and fighter1.get_health_points()<=0:
            result=f"{fighter2.get_army()} wins"
        fighter1.set_health_points(fighter1_health_points)
        fighter2.set_health_points(fighter2_health_points)
        return result





