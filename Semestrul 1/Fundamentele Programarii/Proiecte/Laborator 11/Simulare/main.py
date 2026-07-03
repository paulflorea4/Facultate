from repository.FighterRepository import FighterRepository
from service.FigherController import FigherController
from ui.console import Console

fighter_repository = FighterRepository("Fighters.txt")
fighter_service=FigherController(fighter_repository)
console = Console(fighter_service)
console.run()