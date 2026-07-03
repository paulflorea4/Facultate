from pprint import pprint

from domain.persoana import Persoana

# f = open("persons_default.txt", "r")
# text = f.read()
# f.close()

# with open("persons_default.txt", "r") as file:
#     line = file.readline()
#     cnp, nume = line.split(",")
#     p = Persoana(cnp, nume)
#     print(cnp, nume)

#w - write
#a - append
with open("persons.txt", "a") as file:
    file.write("abjfja")
