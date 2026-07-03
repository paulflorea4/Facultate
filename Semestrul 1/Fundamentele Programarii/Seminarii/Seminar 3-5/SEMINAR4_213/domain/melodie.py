def creare_melodie(titlu: str, artist: str, gen: str, durata: float) -> dict:
    """
    Creeaza melodie pe baza informatiilor date
    :param titlu: titlul melodiei
    :param artist: artistul melodiei
    :param gen: genul melodiei
    :param durata: durata melodiei
    :return: un dictionar care reprezinta meloldia
    """
    return {'titlu': titlu, 'artist': artist, 'gen': gen, 'durata': durata}
    # return [titlu, artist, gen, durata]


def get_titlu(melodie: dict):
    """
    Returneaza titlul melodiei date ca parametru
    :param melodie: melodia pentru care vrem sa accesam titlu
    :return: titlul melodiei
    """
    return melodie['titlu']
    # return melodie[0]


def get_artist(melodie: dict):
    return melodie['artist']


def get_gen(melodie: dict):
    return melodie['gen']


def get_durata(melodie: dict):
    return melodie['durata']


def set_titlu(melodie: dict, titluNou: str):
    melodie['titlu'] = titluNou


def set_artist(melodie: dict, artistNou: str):
    melodie['artist'] = artistNou


def set_gen(melodie: dict, genNou: str):
    melodie['gen'] = genNou


def set_durata(melodie: dict, durataNoua: str):
    melodie['durata'] = durataNoua


def test_creare_melodie():
    melodie1 = creare_melodie('T1', 'A1', 'folk', 3.40)
    assert (get_titlu(melodie1) == "T1")
    assert (get_artist(melodie1) == "A1")
    assert (get_gen(melodie1) == "folk")
    assert (get_durata(melodie1) == 3.40)

