from domain.dtos import SongListens


def test_song_listens():
    song_listens1 = SongListens("Highway Star", "Deep Purple", 1)
    assert (song_listens1.get_artist() == "Deep Purple")
    assert (song_listens1.get_titlu() == "Highway Star")
    assert (song_listens1.get_nr_ascultari() == 1)

    song_listens1.set_titlu("Perfect Strangers")
    assert (song_listens1.get_titlu() == "Perfect Strangers")
    song_listens1.incrementeaza_nr_ascultari()

    assert (song_listens1.get_nr_ascultari() == 2)
