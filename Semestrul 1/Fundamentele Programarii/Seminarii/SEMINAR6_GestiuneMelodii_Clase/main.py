from domain.validation import ValidatorMelodie
from list_management.music_manager import MusicManager
from ui.console import Console

validator = ValidatorMelodie()
music_manager = MusicManager(validator)
console = Console(music_manager)
console.run()
