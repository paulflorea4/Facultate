#include  <iostream>
#include <fstream>
#include <vector>
using namespace std;

const int INF=INT_MAX;
const int maxV=10001;

vector<pair<int,int>> lstAdj[maxV];
int distanta[maxV];
bool vizitat[maxV];

int distantaMinima(int n) {
    int distantaMinima=INF,nod=-1;
    for(int i=0;i<n;i++) {
        if (!vizitat[i] && distanta[i]<distantaMinima) {
            distantaMinima=distanta[i];
            nod=i;
        }
    }
    return nod;
}

void dijkstra(int start,int V) {
    for (int i=0; i<V; i++) {
        distanta[i]=INF;
        vizitat[i]=false;
    }
    distanta[start]=0;

    for (int i=0; i<V; i++) {
        int u=distantaMinima(V);
        vizitat[u]=true;
        for (auto vecin:lstAdj[u]) {
            int v=vecin.first;
            int w=vecin.second;
            if (!vizitat[v] && distanta[u]+w<distanta[v]) {
                distanta[v]=distanta[u]+w;
            }
        }
    }

    for (int i=0; i<V; i++) {
        if (distanta[i]==INF)
            cout<<"INF"<<" ";
        else
            cout<<distanta[i]<<" ";
    }
}

int main() {
    ifstream fin("graf.txt");
    ofstream fout("cost.txt");

    int V,E,S;
    fin>>V>>E>>S;
    int x,y,w;
    while(fin>>x>>y>>w) {
        lstAdj[x].push_back(make_pair(y,w));
    }

    dijkstra(0,V);

    fin.close();
    fout.close();
    return 0;
}