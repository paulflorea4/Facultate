#include <iostream>
#include <fstream>
#include <queue>
#include <vector>

using namespace std;

int main() {
    ifstream fin("graf.txt");
    int n,u,v,sursa;
    fin>>n;
    vector<vector<int>> lstAdj(n);
    while(fin>>u>>v) {
        lstAdj[u].push_back(v);
        lstAdj[v].push_back(u);
    }
    fin.close();
    cout<<"Varful sursa:";
    cin>>sursa;

    vector<int>distanta(n,-1);
    vector<int>parinte(n,-1);
    vector<bool>vizitat(n,false);
    queue<int>coada;
    distanta[sursa]=0;
    coada.push(sursa);
    while(!coada.empty()) {
        int x=coada.front();
        coada.pop();
        for (int i:lstAdj[x]) {
            if (!vizitat[i]) {
                if (distanta[i]==-1) {
                    distanta[i]=distanta[x]+1;
                    parinte[i]=x;
                    coada.push(i);
                }
            }
        }
    }

    cout<<"Arborele descoperit"<<endl;
    for (int i=0;i<n;i++) {
        cout<<"Varful "<< i<<" parinte:"<<parinte[i]<<" distanta fata de sursa: "<<distanta[i]<<endl;
    }
    return 0;
}