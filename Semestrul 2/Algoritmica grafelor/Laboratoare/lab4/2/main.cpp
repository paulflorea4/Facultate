#include <fstream>
#include <vector>
#include <set>
using namespace std;

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int M;
    fin >> M;
    int N = M + 2;

    vector<int> prufer(M);
    vector<int> degree(N, 1);

    for (int i = 0; i < M; ++i) {
        fin >> prufer[i];
        degree[prufer[i]]++;
    }

    set<int> leaves;
    for (int i = 0; i < N; ++i) {
        if (degree[i] == 1) {
            leaves.insert(i);
        }
    }

    vector<int> parent(N, -1);

    for (int i = 0; i < M; ++i) {
        int leaf = *leaves.begin();
        leaves.erase(leaves.begin());

        parent[leaf] = prufer[i];

        degree[leaf]--;
        degree[prufer[i]]--;

        if (degree[prufer[i]] == 1) {
            leaves.insert(prufer[i]);
        }
    }

    auto it = leaves.begin();
    int u = *it++;
    int v = *it;

    parent[u] = v;

    fout << N << "\n";
    for (int i = 0; i < N; ++i) {
        fout << parent[i] << " ";
    }
    fout << "\n";

    return 0;
}
