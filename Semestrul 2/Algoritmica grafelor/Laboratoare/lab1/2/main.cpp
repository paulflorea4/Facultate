#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

void dfs(int nod,vector<vector<int>>& matrAdj,vector<bool>& vizitat) {
    vizitat[nod]=true;
    for (int i = 1; i <= matrAdj[nod].size(); i++) {
        if (matrAdj[nod][i]==1 && !vizitat[i]) {
            dfs(i,matrAdj,vizitat);
        }
    }
}
int main() {
    ifstream fin("input.txt");
    int nrNoduri,n1,n2;
    fin>>nrNoduri;
    vector<vector<int> >matrAdj(nrNoduri+1,vector<int>(nrNoduri+1,0));
    while(fin>>n1>>n2) {
        matrAdj[n1][n2]=1;
        matrAdj[n2][n1]=1;
    }
    cout<<"Noduri izolate:";
    for (int i = 1; i <= nrNoduri; i++) {
        bool izolat=true;
        for (int j = 1; j <= nrNoduri; j++) {
            if (matrAdj[i][j])
                izolat=false;
        }
        if (izolat)
            cout<<i<<" ";
    }
    cout<<endl;
    int grad1=0;
    bool regular=true;
    for (int i = 1; i <= nrNoduri; i++) {
        grad1+=matrAdj[1][i];
    }
    for (int i=2;i<=nrNoduri;i++) {
        int grad=0;
        for (int j=1;j<=nrNoduri;j++) {
            grad+=matrAdj[i][j];
        }
        if (grad!=grad1)
            regular=false;
    }
    if (regular)
        cout<<"Graful este regular"<<endl;
    else
        cout<<"Graful nu este regular"<<endl;

    vector<vector<int> >matrDist(nrNoduri+1,vector<int>(nrNoduri+1,INT_MAX));

    for (int i=1; i<=nrNoduri; i++) {
        for (int j=1; j<=nrNoduri; j++) {
            if (matrAdj[i][j])
                matrDist[i][j]=1;
            if (i==j)
                matrDist[i][j]=0;
        }
    }

    for (int k=1; k<=nrNoduri; k++) {
        for (int i=1; i<=nrNoduri; i++) {
            for (int j=1; j<=nrNoduri; j++) {
                if (matrDist[i][k]!=INT_MAX && matrDist[k][j]!=INT_MAX)
                    matrDist[i][j]=min(matrDist[i][j],matrDist[i][k]+matrDist[k][j]);
            }
        }
    }

    cout<<"Matricea distantelor"<<endl;
    for (int i=1; i<=nrNoduri; i++) {
        for (int j=1; j<=nrNoduri; j++) {
            cout<<matrDist[i][j]<<" ";
        }
        cout<<endl;
    }

    vector<bool> vizitat(nrNoduri+1,false);

    dfs(1,matrAdj,vizitat);
    
    bool conex=true;
    for (int i=1;i<=nrNoduri;i++) {
        if (!vizitat[i]) {
            conex=false;
        }

    }
    if (conex)
        cout<<"Graful este conex"<<endl;
    else
        cout<<"Graful nu este conex"<<endl;
    return 0;
}
