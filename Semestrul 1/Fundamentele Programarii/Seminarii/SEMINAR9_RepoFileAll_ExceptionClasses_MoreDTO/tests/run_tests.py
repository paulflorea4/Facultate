from colorama import Fore, Style

from tests.teste_domain.teste_melodie import test_melodie, test_validare_melodie, test_equal_melodie
from tests.teste_domain.teste_persoana import test_persoana, test_equal_persoana, test_validare_persoana
from tests.teste_domain.teste_rating import test_create_rating, test_equal_rating
from tests.teste_repository.teste_repo_melodii import test_update_repo, test_find_repo, test_store_repo
from tests.teste_repository.teste_repo_persons import test_read_from_file, test_store_repo_file, test_find_repo_file, \
    test_delete_repo_file
from tests.teste_service.teste_service_evaluari import test_most_listened_to, test_add_evaluare
from tests.teste_service.teste_service_melodii import test_add_service, test_actualizeaza_service, test_filter_service


def run_tests_service():
    test_add_service()
    test_actualizeaza_service()
    test_filter_service()
    test_add_evaluare()
    test_most_listened_to()
    print("[INFO] " + Fore.GREEN + "Teste service rulate cu succes" + Style.RESET_ALL)


def run_tests_repos():
    test_store_repo()
    test_find_repo()
    test_update_repo()
    test_read_from_file()
    test_store_repo_file()
    test_find_repo_file()
    test_delete_repo_file()
    print("[INFO] " + Fore.GREEN + "Teste repo rulate cu succes" + Style.RESET_ALL)


def run_tests_domain():
    test_melodie()
    test_validare_melodie()
    test_equal_melodie()
    test_persoana()
    test_equal_persoana()
    test_validare_persoana()
    test_create_rating()
    test_equal_rating()
    print("[INFO] " + Fore.GREEN + "Teste domain rulate cu succes" + Style.RESET_ALL)


def run_tests_all():
    run_tests_domain()
    run_tests_service()
    run_tests_repos()
