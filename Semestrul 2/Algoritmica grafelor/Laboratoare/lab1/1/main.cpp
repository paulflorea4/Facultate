#include <iostream>
#include <fstream>
#include <set>
#include <vector>

using namespace std;

int main()
{
    ifstream fin("input.txt");
    int nrNoduri,n1,n2;
    fin>>nrNoduri;
    vector<vector<int>> matrAdj(nrNoduri+1, vector<int>(nrNoduri+1, 0));
    cout<<"Lista"<<endl;
    while (fin>>n1>>n2) {
        matrAdj[n1][n2] = 1;
        matrAdj[n2][n1] = 1;
        cout<<n1<<" "<<n2<<endl;
    }
    fin.close();
    cout<<"Matricea de adiacenta"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        for (int j=1;j<=nrNoduri;j++) {
            cout << matrAdj[i][j] << " ";
        }
        cout << endl;
    }

    vector<vector<int>> listAdj(nrNoduri+1);
    for (int i=1;i<=nrNoduri;i++) {
        for (int j=1;j<=nrNoduri;j++) {
            if (matrAdj[i][j]) {
                listAdj[i].push_back(j);
            }
        }
    }

    set<pair<int,int>> muchii;

    cout<<"Lista de adiacenta"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        cout<<i<<": ";
        for (int j:listAdj[i]) {
            if (i<j)
                muchii.insert(make_pair(i,j));
            cout<<j<<" ";
        }

        cout<<endl;
    }

    vector<vector<int>> matrInc(nrNoduri+1,vector<int>(muchii.size()+1,0));
    int index=0;
    for (pair<int,int> muchie:muchii) {
        index++;
        matrInc[muchie.first][index]=1;
        matrInc[muchie.second][index]=1;
    }

    cout<<"Matrice de incidenta"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        for (int j=1;j<=muchii.size();j++) {
            cout<<matrInc[i][j]<<" ";
        }
        cout<<endl;
    }

    vector<vector<int>> listAdj2(nrNoduri+1);

    for (pair <int,int> muchie:muchii) {
        listAdj2[muchie.first].push_back(muchie.second);
        listAdj2[muchie.second].push_back(muchie.first);
    }

    vector<vector<int>> matrAdj2(nrNoduri+1, vector<int>(nrNoduri+1, 0));

    cout<<"Lista de adiacenta"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        cout<<i<<": ";
        for (int j:listAdj[i]) {
            matrAdj2[i][j]=1;
            matrAdj2[j][i]=1;
            cout<<j<<" ";
        }
        cout<<endl;
    }

    cout<<"Matricea de adiacenta"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        for (int j=1;j<=nrNoduri;j++) {
            cout<<matrAdj2[i][j]<<" ";
        }
        cout<<endl;
    }

    cout<<"Lista"<<endl;
    for (int i=1;i<=nrNoduri;i++) {
        for (int j=i+1;j<=nrNoduri;j++) {
            if (matrAdj[i][j]) {
                cout<<i<<" "<<j<<endl;
            }
        }
    }
    return 0;
}