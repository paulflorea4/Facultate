#include <iostream>
#include <queue>
#include <unordered_map>
#include <vector>
using namespace std;

struct Nod {
    char caracter;
    int frecventa;
    Nod *stanga, *dreapta;

    Nod(char c, int f) : caracter(c), frecventa(f), stanga(nullptr), dreapta(nullptr) {}
};

struct Compara {
    bool operator()(Nod* a, Nod* b) {
        return a->frecventa > b->frecventa;
    }
};

void construiesteCoduri(Nod* radacina, string cod, unordered_map<char, string>& coduriHuffman) {
    if (!radacina)
        return;
    
    if (!radacina->stanga && !radacina->dreapta)
        coduriHuffman[radacina->caracter] = cod;

    construiesteCoduri(radacina->stanga, cod + "0", coduriHuffman);
    construiesteCoduri(radacina->dreapta, cod + "1", coduriHuffman);
}

string codare(const string& textOriginal, unordered_map<char, string>& coduriHuffman, Nod*& radacina) {
    unordered_map<char, int> frecvente;
    
    for (char c : textOriginal)
        frecvente[c]++;
    
    priority_queue<Nod*, vector<Nod*>, Compara> coada;
    
    for (auto& pereche : frecvente)
        coada.push(new Nod(pereche.first, pereche.second));
    
    while (coada.size() > 1) {
        Nod* st = coada.top();
        coada.pop();
        Nod* dr = coada.top();
        coada.pop();

        Nod* combinat = new Nod('\0', st->frecventa + dr->frecventa);
        combinat->stanga = st;
        combinat->dreapta = dr;

        coada.push(combinat);
    }

    radacina = coada.top();
    construiesteCoduri(radacina, "", coduriHuffman);
    
    string textCodificat = "";
    for (char c : textOriginal)
        textCodificat += coduriHuffman[c];

    return textCodificat;
}

string decodare(const string& textCodificat, Nod* radacina) {
    string textDecodificat = "";
    Nod* curent = radacina;

    for (char bit : textCodificat) {
        if (bit == '0')
            curent = curent->stanga;
        else
            curent = curent->dreapta;
        
        if (!curent->stanga && !curent->dreapta) {
            textDecodificat += curent->caracter;
            curent = radacina;
        }
    }

    return textDecodificat;
}

int main() {
    string text = "Treeaaassuureee";

    unordered_map<char, string> coduriHuffman;
    Nod* radacina = nullptr;
    
    string codificat = codare(text, coduriHuffman, radacina);
    
    cout << "Coduri Huffman:\n";
    for (auto& pereche : coduriHuffman)
        cout << pereche.first << ": " << pereche.second << "\n";

    cout<<endl;
    cout << "Text comprimat: " << codificat << '\n';
    
    string original = decodare(codificat, radacina);
    cout << "Text decompresat: " << original << '\n';

    return 0;
}
