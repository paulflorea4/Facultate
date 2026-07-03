def creare_melodie(titlu: str, artist: str, gen: str, durata: float) -> dict:
    """
    Creeaza melodie pe baza informatiilor date
    :param titlu: titlul melodiei
    :param artist: artistul melodiei
    :param gen: genul melodiei
    :param durata: durata melodiei
    :return: un dictionar care reprezinta melodia
    """
    return {'titlu': titlu, 'artist': artist, 'gen': gen, 'durata': durata}
    # return [titlu, artist, gen, durata]


def get_titlu(melodie: dict) -> str:
    """
    Returneaza titlul melodiei date ca parametru
    :param melodie: melodia pentru care vrem sa accesam titlu
    :return: titlul melodiei
    """
    return melodie['titlu']
    # return melodie[0]


def get_artist(melodie: dict) -> str:
    return melodie['artist']


def get_gen(melodie: dict) -> str:
    return melodie['gen']


def get_durata(melodie: dict) -> float:
    return melodie['durata']


def set_titlu(melodie: dict, titlu_nou: str):
    melodie['titlu'] = titlu_nou


def set_artist(melodie: dict, artist_nou: str):
    melodie['artist'] = artist_nou


def set_gen(melodie: dict, gen_nou: str):
    melodie['gen'] = gen_nou


def set_durata(melodie: dict, durata_noua: str):
    melodie['durata'] = durata_noua
