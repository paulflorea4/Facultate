from domain.spectacol import Spectacol


class Validator:
    def validate(self,spectacol:Spectacol):
        """
        Functie de validare a atributelor obiectului de tip spectacol
        :param spectacol: obiectul de validat
        :return: -
        :raises: ValueError daca obiectul nu respecta condiitle date
        """
        errors=[]
        if spectacol.titlu()=="":
            errors.append("Titul nu poate sa fie vid")
        if spectacol.artist()=="":
            errors.append("Artistul nu poate sa fie vid")
        if spectacol.durata<=0:
            errors.append("Durata trebuie sa fie numar strict pozitiv")
        if spectacol.gen not in ["Comedie","Concert","Balet","Altele"]:
            errors.append("Spectacolul trebuie sa fie una dintre comedie,concert,balet,altele")

        if len(errors)>0:
            error_msg='\n'.join(errors)
            raise ValueError(error_msg)