#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

int main() {
    ifstream fin("graf.txt");
    int n;
    fin>>n;
    vector<vector<int>> matrAdj(n,vector<int>(n));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            fin>>matrAdj[i][j];
        }
    }

    for (int k = 0; k < n; k++) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrAdj[i][j] = matrAdj[i][j] || (matrAdj[i][k] && matrAdj[k][j]);
            }
        }
    }

    cout << "Matricea inchiderii tranzitive: \n";
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cout << matrAdj[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}