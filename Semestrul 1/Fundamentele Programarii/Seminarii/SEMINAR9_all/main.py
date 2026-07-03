from domain.validation import ValidatorMelodie, ValidatorPersoana, ValidatorRating
from repository.repo_persoane import PersonFileRepository
from repository.repo_ratings import RatingMemoryRepository
from repository.repo_melodii import SongMemoryRepository
from service.person_controller import ServicePersoane
from service.rating_controller import ServiceRatings
from service.song_controller import ServiceMelodii
from tests.run_tests import run_tests_all
from ui.console import Console

run_tests_all()

validator_melodii = ValidatorMelodie()
validator_persoane = ValidatorPersoana()
validator_evaluari = ValidatorRating()

repo_melodii = SongMemoryRepository()
repo_persoane = PersonFileRepository("persons.txt")
repo_ratings = RatingMemoryRepository()

song_service = ServiceMelodii(repo_melodii, validator_melodii)
person_service = ServicePersoane(repo_persoane, validator_persoane)
rating_service = ServiceRatings(repo_melodii, repo_persoane, repo_ratings, validator_evaluari)

console = Console(song_service, person_service, rating_service)
console.run()


