#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <queue>
using namespace std;

int main(int argc, char* argv[]) {
    ifstream in(argv[1]);
    ofstream out(argv[2]);

    int N;
    in >> N;

    vector<int> parent(N);
    vector<vector<int>> tree(N);
    vector<int> degree(N, 0);

    for (int i = 0; i < N; ++i) {
        in >> parent[i];
        if (parent[i] != -1) {
            tree[parent[i]].push_back(i);
            tree[i].push_back(parent[i]);
            degree[parent[i]]++;
            degree[i]++;
        }
    }

    set<int> leaves;
    for (int i = 0; i < N; ++i) {
        if (degree[i] == 1) {
            leaves.insert(i);
        }
    }

    vector<int> pruferCode;
    for (int i = 0; i < N - 2; ++i) {
        int leaf = *leaves.begin();
        leaves.erase(leaves.begin());

        for (int neighbor : tree[leaf]) {
            if (degree[neighbor] > 0) {
                pruferCode.push_back(neighbor);
                degree[leaf]--;
                degree[neighbor]--;
                if (degree[neighbor] == 1) {
                    leaves.insert(neighbor);
                }
                break;
            }
        }
    }

    out << pruferCode.size() << "\n";
    for (int i = 0; i < pruferCode.size(); ++i) {
        if (i > 0) out << " ";
        out << pruferCode[i];
    }
    out << "\n";

    return 0;
}
