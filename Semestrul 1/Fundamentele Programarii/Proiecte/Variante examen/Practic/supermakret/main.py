from repository.repo_produse import RepoProduse
from business.service_produse import ServiceProduse
from validator.validator_produse import ValidatorProdus
from ui.ui import UI

repo = RepoProduse("produse.txt")

validator = ValidatorProdus()
service = ServiceProduse(repo, validator)

ui = UI(service)

ui.run()

