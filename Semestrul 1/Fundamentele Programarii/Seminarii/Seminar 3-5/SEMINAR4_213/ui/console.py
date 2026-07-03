from domain.melodie import creare_melodie
from list_management.list_manager import add_to_list, cauta_melodie, sterge_melodie, ruleaza_teste, elimina_dupa_gen, \
    filtreaza_dupa_durata, add_default_songs


def afiseaza_meniu():
    print("1. Adauga melodie la lista")
    print("2. Cauta melodie dupa titlu si artist")
    print("3. Stergea unui cantec dupa titlu si artist")
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
    durata = float(durata)
    return titlu, artist, gen, durata


def afiseaza_melodii(lista_melodii):
    for i, song in enumerate(lista_melodii):
        # end = "": don't jump to new line, end fiind caracterul
        # afisat dupa ultima valoare de afisat (by default, end=new line
        # de aceea de obicei print() afiseaza ce ii dam si apoi se trece la linie noua)
        print("Melodie #" + str(i) + ": ", end="")
        # construim un string cu informatiile unei melodii
        # (doar pentru exersare lucru cu str, se poate printa direct)
        song_info = ""
        for key, value in song.items():
            song_info += key.capitalize() + ": " + str(value) + " | "
        print(song_info)


def sterge_dupa_gen_ui(lista_melodii: list):
    gen_de_sters = input("Genul dupa care se sterge: ").strip().lower()
    if elimina_dupa_gen(lista_melodii, gen_de_sters):
        print("S-au sters melodii din genul", gen_de_sters)
    else:
        print("Nu au existat melodii din genul", gen_de_sters, "pentru a fi sterse.")


def filtreaza_dupa_durata_ui(lista_melodii):
    durata_s = input("Limita inferioara pentru durata este: ")
    durata_f = input("Limita superioara pentru durata este: ")

    durata_s = float(durata_s)
    durata_f = float(durata_f)
    lista_filtrata = filtreaza_dupa_durata(lista_melodii, durata_s, durata_f)
    print("Melodiile care au durata intre", durata_s, "si", durata_f, "sunt:")
    afiseaza_melodii(lista_filtrata)


def run():
    ruleaza_teste()
    lista_melodii = []
    is_running = True
    while is_running:
        afiseaza_meniu()
        optiune = input(">>>").upper().strip()
        match optiune:
            case '1':
                # add la lista
                # F1: Adaugare in lista
                #   T1: citire informatii --done
                #   T2: crearea melodiei pe baza informatii
                #   T3: adaugare in lista
                #   T4: interactiune cu utilizatorul
                titlu, artist, gen, durata = citeste_info_melodie()
                melodie = creare_melodie(titlu, artist, gen, durata)
                add_to_list(lista_melodii, melodie)
            case '2':
                # cautare in lista
                titlu_cautat = input("Titlul melodiei cautate: ")
                artist_cautat = input("Artistul melodiei cautate: ")
                melodie_cautata = cauta_melodie(lista_melodii, titlu_cautat, artist_cautat)
                if melodie_cautata is not None:
                    print("Melodia a fost gasita, acestea sunt toate informatiile despre ea:", melodie_cautata)
                else:
                    print("Melodia nu a fost gasita in lista.")
            case '3':
                # stergere din lista
                titlu_de_sters = input("Titlul melodiei de sters:")
                artist_de_sters = input("Artistul melodiei de sters: ")
                sterge_melodie(lista_melodii, titlu_de_sters, artist_de_sters)
                # cum sa verificam daca s-a sters ceva pentru a afisa mesaj de succes
                # sau mesaj ca nu s-a gasit melodia in lista?
            case '4':
                sterge_dupa_gen_ui(lista_melodii)
            case '5':
                filtreaza_dupa_durata_ui(lista_melodii)
            case 'P':
                afiseaza_melodii(lista_melodii)
            case 'D':
                add_default_songs(lista_melodii)
                print("S-au adaugat melodiile default.")
            case 'E':
                is_running = False
