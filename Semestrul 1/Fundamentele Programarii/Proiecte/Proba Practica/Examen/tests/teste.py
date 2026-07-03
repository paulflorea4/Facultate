from domain.spectacol import Spectacol
from domain.validator import Validator
from repository.RepoSpectacole import RepoSpectacole
from service.ServSpectacole import SpectacoleService


def teste_domain():
    """
    Functie de testare pentru domain
    :return: -
    """
    spectacol=Spectacol("titlu","artist","Comedie",3)
    assert spectacol.titlu()=="titlu"
    assert spectacol.artist()=="artist"
    assert spectacol.gen=="Comedie"
    assert spectacol.durata==3
    spectacol.durata=4
    assert spectacol.durata==4

def teste_repo():
    """
    Functie de testare pentru repository
    :return: -
    """
    repo=RepoSpectacole("teste.txt")
    spectacol = Spectacol("titlu", "artist", "Comedie", 3)
    repo.store(spectacol)
    assert repo.get_len()==1
    spectacol = Spectacol("titlu", "artist", "Comedie", 3)
    try:
        repo.store(spectacol)
        assert False
    except ValueError:
        assert True
    spectacol=Spectacol("titlu", "artist", "Comedie", 4)
    repo.modify(spectacol)
    spectacol2=Spectacol("titlu", "artist2", "Drama", 5)
    assert repo.get_len()==1
    try:
        repo.modify(spectacol2)
        assert False
    except ValueError:
        assert True
    repo.empty_file()

def teste_service():
    """
    Functie de testare pentru service
    :return: -
    """
    repo = RepoSpectacole("teste.txt")
    validator=Validator()
    service=SpectacoleService(repo,validator)
    try:
        service.adauga_spectacol("titlu","artist","Comedie",-2)
        assert False
    except ValueError:
        assert True

    try:
        service.adauga_spectacol("titlu","","Comedie",3)
        assert False
    except ValueError:
        assert True

    try:
        service.modifica_spectacol("titlu","artist","Drama",3)
        assert False
    except ValueError:
        assert True

    service.adauga_spectacol("titlu","artist","Comedie",3)
    assert service.get_len()==1
    service.empty_file()


def run_all_tests():
    teste_domain()
    teste_repo()
    teste_service()
    print("Teste rulate cu succes")


