from errors import ValidationError

class ValidatorProdus:
    def valideaza(self, produs):
        id = produs.id()
        denumire = produs.denumire()
        pret = produs.pret()

        errors = []

        if not id.isdigit():
            errors.append("Id-ul trebuie sa contina doar cifre")

        if pret <= 0:
            errors.append("Pretul trebue sa fie un numar mai mare decat 0 strict")

        if denumire == "":
            errors.append("Denumirea nu poate fi vida")

        if len(errors)>0:
            error_message = ",".join(errors)
            raise ValidationError(error_message)