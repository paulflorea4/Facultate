from domain.validation import ValidatorMelodie
from repository.repository import RepositoryMelodii
from service.controller import ControllerMelodii
from tests.run_tests import run_tests_all
from ui.console import Console

run_tests_all()

validator = ValidatorMelodie()
repo = RepositoryMelodii()
song_service = ControllerMelodii(repo, validator)
console = Console(song_service)
console.run()


