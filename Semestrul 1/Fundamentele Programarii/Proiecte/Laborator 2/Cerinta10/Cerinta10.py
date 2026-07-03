def numarMinim(x):
    if x == 0:
        return 0
    cif = [];
    m = 0;
    cif0 = 1
    while x > 0:
        if x % 10 == 0:
            cif0 *= 10
        else:
            cif.append(x % 10)
        x = x // 10
    cif.sort()
    m = cif[0]
    m *= cif0
    for i in range(1, len(cif)):
        m = m * 10 + cif[i]
    return m


def test_numarMinim():
    assert numarMinim(0) == 0
    assert numarMinim(312) == 123
    assert numarMinim(9104) == 1049


n = int(input("Introduceti valoarea lui n: "))
test_numarMinim()
m = numarMinim(n)
print(f"Numarul minim format cu cifrele lui {n} este {m}")
