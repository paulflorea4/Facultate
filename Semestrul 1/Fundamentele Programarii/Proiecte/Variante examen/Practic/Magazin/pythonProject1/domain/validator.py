from domain.produs import Produs


class ValidatorProdus:
    def valideaza(self,produs:Produs):
        errors=[]

        if not produs.id().isdigit():
            errors.append("Id-ul trebuie sa contina doar cifre")
        if produs.pret()<=0:
            errors.append("Pretul trebuie sa fie mai mare ca 0")
        if produs.denumire()=="":
            errors.append("Denumirea nu poate fi vida")

        if len(errors)>0:
            error_message="\n".join(errors)
            raise ValueError(error_message)
