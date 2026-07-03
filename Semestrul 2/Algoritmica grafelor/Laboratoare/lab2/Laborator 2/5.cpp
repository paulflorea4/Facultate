#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void DFS_VISIT(vector<vector<int>> &adj,vector<bool> &vizitat,int u) {
    vizitat[u]=true;
    cout<<u<<" ";
    for (int v : adj[u]) {
        if (vizitat[v]==false) {
            DFS_VISIT(adj,vizitat,v);
        }
    }
}

void DFS(vector<vector<int>> &adj,int n) {
    vector<bool> vizitat(n,false);
    cout<<"Padurea descoperita de DFS:\n";
    for(int i=0;i<n;i++) {
        if(vizitat[i]==false) {
            cout<<"Componenta: ";
            DFS_VISIT(adj,vizitat,i);
            cout<<"\n";
        }
    }
}
int main() {
    ifstream fin("graf.txt");
    int n,u,v;
    fin >> n;
    vector<vector<int>>adj(n);
    while(fin >> u >> v) {
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    fin.close();

    DFS(adj,n);
    return 0;
}