from Business.VehicleService import Service
from Infrastructure.VehicleRepository import FileRepository
from Tests.Tests import runAllTests
from UserInterface.Menu import Menu

def main():
    """
    Functia principala a aplicatiei care coordoneaza executia acesteia
    """
    runAllTests()
    repository = FileRepository("date.txt")
    service = Service(repository)
    menu = Menu(service)
    menu.run()

if __name__ == '__main__':
    main()
