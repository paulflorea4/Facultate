#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

vector<pair<int,int>> graf[10001];
int dist[10001];

void relax(int u,int v,int w) {
    if (dist[u]!=INT_MAX && dist[v]>dist[u]+w)
        dist[v]=dist[u]+w;
}

bool Bellman_Ford(int sursa,int n){
    for (int i=0;i<n;i++){
        dist[i]=INT_MAX;
    }
    dist[sursa]=0;
    for(int u=0;u<n-1;u++){
        for(auto vecin:graf[u]){
            int v=vecin.first;
            int w=vecin.second;
            relax(u,v,w);
        }
    }
    for(int u=0;u<n;u++) {
        for (auto vecin:graf[u]) {
            int v=vecin.first;
            int w=vecin.second;
            if (dist[u]!=INT_MAX && dist[v]>dist[u]+w) {
                return false;
            }
        }
    }
    return true;
}

int main(int argc,char *argv[]){
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int v,e,s;
    fin>>v>>e>>s;

    for(int i=0;i<e;i++){
        int x,y,w;
        fin>>x>>y>>w;
        graf[x].push_back(make_pair(y,w));
    }

    if(Bellman_Ford(s,v )) {
        for (int i=0;i<v;i++) {
            if(dist[i]==INT_MAX)
                fout<<"INT"<<" ";
            else
                fout<<dist[i]<<" ";
        }
    }
    return 0;
}