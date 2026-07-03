#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

const int INF=INT_MAX;

int main() {
    ifstream fin("input.txt");
    int n, m,gasit=0;
    fin >> n >> m;

    vector<vector<int>> dist(n + 1, vector<int>(n + 1, INF));

    for (int i = 1; i <= n; i++)
        dist[i][i] = 0;

    for (int i = 0; i < m; i++) {
        int u, v, cost;
        fin >> u >> v >> cost;
        dist[u][v] = cost;
    }

    for (int k = 1; k <= n; k++)
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (dist[i][k] < INF && dist[k][j] < INF)
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);

    for (int i = 1; i <= n; i++) {
        if (dist[i][i] < 0) {
            cout << "Exista un ciclu negativ" << endl;
            gasit=1;
        }
    }
    if (!gasit) {
        for (int i = 1; i <= n; i++)
        {
            for (int j = 1; j <= n; j++) {
                cout<<"Distanta de la "<< i<< " la "<< j<< " este: ";
                if (dist[i][j] == INF)
                    cout<<"INF"<<endl;
                else
                    cout<<dist[i][j]<<endl;
            }
        }
    }

    return 0;
}