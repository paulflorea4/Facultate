from colorama import Fore, Style

from list_management.music_manager import *


def afiseaza_meniu():
    print("1. Adauga melodie la lista")
    print("2. Cauta melodie dupa titlu si artist")
    print("3. Stergerea unui cantec dupa titlu si artist")
    print("4. Sterge toate melodiile care au un gen dat")
    print("5. Afiseaza toate melodiile care au durata intre doua durate date")
    print("D. Adauga melodii default")
    print("P. Afiseaza lista de melodii")
    print("E. Iesire din aplicatie")


def citeste_info_melodie() -> tuple:
    titlu = input("Introduceti titlul melodiei:")
    artist = input("Introduceti artist melodiei:")
    gen = input("Introduceti genul melodiei:")
    durata = input("Introduceti durata melodiei:")
    #acum putem valida si input-ul
    #ce exceptie arunca aceasta linie daca nu
    #se poate efectua conversia?
    durata = float(durata)
    return titlu, artist, gen, durata


def afiseaza_melodii(lista_melodii):
    for i, song in enumerate(lista_melodii):
        print("Melodie #" + str(i) + ": ", end="")
        song_info = ""
        for key, value in song.items():
            song_info += key.capitalize() + ": " + str(value) + " | "
        print(song_info)


def sterge_dupa_gen_ui(music_manager):
    gen_de_sters = input("Genul dupa care se sterge: ").strip().lower()
    elimina_dupa_gen(music_manager, gen_de_sters)
    # am putea returna numarul de melodii sterse pentru a informa utilizatorul
    print("S-au sters melodii din genul", gen_de_sters)


def filtreaza_dupa_durata_ui(music_manager):
    durata_s = input("Limita inferioara pentru durata este: ")
    durata_f = input("Limita superioara pentru durata este: ")

    durata_s = float(durata_s)
    durata_f = float(durata_f)
    lista_filtrata = filtreaza_dupa_durata(music_manager, durata_s, durata_f)
    print("Melodiile care au durata intre", durata_s, "si", durata_f, "sunt:")
    afiseaza_melodii(lista_filtrata)


def undo_ui(music_manager):
    try:
        undo(music_manager)
    except ValueError as e:
        # coloram textul de eroare in rosu
        # necesita modulul colorama
        # https://pypi.org/project/colorama/
        print(Fore.RED + str(e) + Style.RESET_ALL)


def adauga_melodie_ui(music_manager):
    titlu, artist, gen, durata = citeste_info_melodie()

    try:
        adauga_melodie(music_manager, titlu, artist, gen, durata)
    except ValueError as ve:
        print(Fore.RED + str(ve) + Style.RESET_ALL)


def cautare_melodie_ui(music_manager):
    titlu_cautat = input("Titlul melodiei cautate: ")
    artist_cautat = input("Artistul melodiei cautate: ")
    melodie_cautata = cauta_melodie(music_manager, titlu_cautat, artist_cautat)
    if melodie_cautata is not None:
        print("Melodia a fost gasita, acestea sunt toate informatiile despre ea:", melodie_cautata)
    else:
        print("Melodia nu a fost gasita in lista.")


def sterge_melodie_ui(music_manager):
    titlu_de_sters = input("Titlul melodiei de sters:")
    artist_de_sters = input("Artistul melodiei de sters: ")
    sterge_melodie(music_manager, titlu_de_sters, artist_de_sters)


def run():
    # music manager contine si lista curenta de melodii, si lista de undo
    music_manager = creeaza_music_manager()
    is_running = True
    while is_running:
        afiseaza_meniu()
        optiune = input(">>>").upper().strip()
        match optiune:
            case '1':
                adauga_melodie_ui(music_manager)
            case '2':
                # cautare in lista
                cautare_melodie_ui(music_manager)
            case '3':
                sterge_melodie_ui(music_manager)
            case '4':
                sterge_dupa_gen_ui(music_manager)
            case '5':
                filtreaza_dupa_durata_ui(music_manager)
            case '6':
                undo_ui(music_manager)
            case 'P':
                afiseaza_melodii(get_lista_melodii(music_manager))
            case 'D':
                add_default_songs(music_manager)
                print("S-au adaugat melodiile default.")
            case 'E':
                is_running = False
