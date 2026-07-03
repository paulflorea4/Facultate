from domain.validator import ValidatorProdus
from repository.repo_produse import RepoProduse
from service.serv_produse import ServiceProduse
from ui.console import Console

repo=RepoProduse("produse.txt")
validator=ValidatorProdus()
service=ServiceProduse(repo,validator)
console=Console(service)

console.run()