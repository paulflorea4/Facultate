from colorama import Fore, Style

from tests.teste_melodie import test_melodie, test_validare
from tests.teste_repository import test_update_repo, test_find_repo, test_store_repo
from tests.teste_service import test_add_service, test_actualizeaza_service, test_filter_service


def run_tests_service():
    test_add_service()
    test_actualizeaza_service()
    test_filter_service()
    print("[INFO] " + Fore.GREEN + "Teste service rulate cu succes" + Style.RESET_ALL)


def run_tests_repos():
    test_store_repo()
    test_find_repo()
    test_update_repo()
    print("[INFO] " + Fore.GREEN + "Teste repo rulate cu succes" + Style.RESET_ALL)


def run_tests_domain():
    test_melodie()
    test_validare()
    print("[INFO] " + Fore.GREEN + "Teste domain rulate cu succes" + Style.RESET_ALL)


def run_tests_all():
    run_tests_domain()
    run_tests_service()
    run_tests_repos()
