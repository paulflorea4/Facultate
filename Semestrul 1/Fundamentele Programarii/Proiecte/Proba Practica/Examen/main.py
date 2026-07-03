from domain.validator import Validator
from repository.RepoSpectacole import RepoSpectacole
from service.ServSpectacole import SpectacoleService
from ui.console import Console
from tests.teste import run_all_tests

repo=RepoSpectacole("spectacole.txt")
validator=Validator()
service=SpectacoleService(repo,validator)
console=Console(service)

run_all_tests()
console.run()