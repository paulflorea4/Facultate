#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <stack>
using namespace std;

int main() {
    ifstream fin("graf.txt");
    int n,u,v;
    fin>>n;
    vector<vector<int>> adj(n);

    while(fin>>u>>v) {
        adj[u].push_back(v);
    }
    fin.close();

    int sursa,destinatie;;
    cout<<"Introduceti nodul sursa:";
    cin>>sursa;
    cout<<"Introduceti nodul destinatie:";
    cin>>destinatie;

    vector<int> distanta(n,-1);
    vector<int> parinte(n,-1);
    queue<int> coada;

    distanta[sursa]=0;
    coada.push(sursa);
    while(!coada.empty()) {
        int x=coada.front();
        coada.pop();
        for (int y:adj[x]) {
            if(distanta[y]==-1) {
                distanta[y]=x+1;
                parinte[y]=x;
                coada.push(y);
            }
        }
    }
    if (distanta[destinatie]==-1)
        cout<<"Nu exista drum de la "<<sursa<<" la "<<destinatie<<endl;
    else {
        stack<int>lant;
        for (int vf=destinatie;vf!=-1;vf=parinte[vf])
            lant.push(vf);

        cout<<"Cel mai scurt drum: ";
        while (!lant.empty()) {
            cout<<lant.top()<<" ";
            lant.pop();
        }
    }
    return 0;
}