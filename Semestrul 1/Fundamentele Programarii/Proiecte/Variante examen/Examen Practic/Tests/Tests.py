import datetime

from Business.VehicleService import Service
from Domain.Vehicle import Vehicle, VehicleValidator
from Infrastructure.VehicleRepository import MemoryRepository, FileRepository


def runAllTests():
    """
    Functia apeleaza metodele de testare ale aplicatiei
    """
    runDomainTests()
    runInfrastructureTests()
    runBusinessTests()

def runDomainTests():
    """
    Functia testeaza domeniul aplicatiei
    """
    v = Vehicle(1, "Opel", 1500, "Astra", datetime.datetime.today())
    assert v.get_id() == 1
    assert v.get_marca() == "Opel"
    assert v.get_pret() == 1500
    assert v.get_model() == "Astra"
    assert v.get_data() == datetime.datetime.today()

    validator = VehicleValidator()

    assert validator.validate_id(1) == True
    assert validator.validate_marca("Opel") == True
    assert validator.validate_pret(1500) == True
    assert validator.validate_model("Astra") == True
    assert validator.validate_data("15:05:1998") == True

    assert validator.validate_id(-1) == False
    assert validator.validate_marca("") == False
    assert validator.validate_pret(0) == False
    assert validator.validate_model("") == False
    assert validator.validate_data("1998:05:15") == False

def runInfrastructureTests():
    """
    Functia testeaza infrastructura aplicatiei
    """
    repository = MemoryRepository()

    assert repository.get_all_vehicles() == {}
    repository.add_vehicle(Vehicle(1, "Opel", 1500, "Astra", datetime.datetime.today()))
    assert repository.get_all_vehicles() == {1 : Vehicle(1, "Opel", 1500, "Astra", datetime.datetime.today())}
    try:
        repository.add_vehicle(Vehicle(1, "Test", 1300, "Astra", datetime.datetime.today()))
        assert False
    except Exception:
        assert True
    assert repository.get_vehicle_by_id(1) == Vehicle(1, "Opel", 1500, "Astra", datetime.datetime.today())
    try:
        repository.get_vehicle_by_id(2) == Vehicle(1, "Opel", 1500, "Astra", datetime.datetime.today())
        assert False
    except Exception:
        assert True
    repository.remove_vehicle_by_id(1)
    assert repository.get_all_vehicles() == {}
    try:
        repository.remove_vehicle_by_id(2)
        assert False
    except Exception:
        assert True

    repository = FileRepository("fisier_teste.txt")

    assert len(repository.get_all_vehicles()) == 5
    assert repository.get_vehicle_by_id(1) == Vehicle(1, "Opel", 1500, "Astra", datetime.datetime(2022, 5, 10))
    repository.remove_vehicle_by_id(1)
    try:
        repository.get_vehicle_by_id(1) == Vehicle(1, "Opel", 1500, "Astra", datetime.datetime(2022, 5, 10))
        assert False
    except Exception:
        assert True
    assert len(repository.get_all_vehicles()) == 4
    repository.add_vehicle(Vehicle(1, "Opel", 1500, "Astra", datetime.datetime(2022, 5, 10)))

def runBusinessTests():
    """
    Functia testeaza business-ul aplicatiei
    """
    repository = FileRepository("fisier_teste.txt")
    service = Service(repository)

    service.add_vehicle(16, "Opel", 1500, "Astra", datetime.datetime.today().strftime("%d:%m:%Y"))
    assert service.delete_vehicle(6) == 1

    try:
        service.add_vehicle(5, "Opel", 1500, "Astra", datetime.datetime.today().strftime("%d:%m:%Y"))
        assert False
    except Exception:
        assert True

    assert service.delete_vehicle(6) == 0

    try:
        service.delete_vehicle(61) == 0
        assert False
    except Exception:
        assert True

    service.filter_vehicles("a", 1600)

    try:
        service.filter_vehicles("a", -5)
        assert False
    except Exception:
        assert True

    dict = service.get_filtered_vehicles()
    assert len(service.get_filtered_vehicles()) == 3

    service.filter_vehicles("", -1)
    assert len(service.get_filtered_vehicles()) == 7

    service.undo()
    service.undo()
    assert len(service.get_filtered_vehicles()) == 7

    try:
        service.undo()
        assert False
    except Exception:
        assert True

    assert service.delete_vehicle(6) == 1


