#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

vector<pair<int,int>> graf[10001];
int dist[10001];
int parinte[10001];

bool Bellman_Ford(int sursa,int n){
    for (int i=0;i<n;i++){
          dist[i]=INT_MAX;
          parinte[i]=-1;
    }
    dist[sursa]=0;
    for(int i=0;i<n;i++){
      for(auto vecin:graf[i]){

      }
    }
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
    return 0;
}