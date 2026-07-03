#include <iostream>
#include <fstream>
#include <queue>
#include <vector>

using namespace std;

vector<vector<char>> maze;
vector<vector<int>> dist;
int dx[] = {-1, 1, 0, 0};
int dy[] = {0, 0, 1, -1};

ifstream fin("labirint1.txt");

void leeBFS(int startX, int startY, int rows, int cols) {
    queue<pair<int, int>> q;
    q.push({startX, startY});
    dist[startX][startY] = 0;

    while (!q.empty()) {
        auto [x, y] = q.front();
        q.pop();

        for (int k = 0; k < 4; k++) {
            int newX = x + dx[k];
            int newY = y + dy[k];

            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols &&
                maze[newX][newY] != '1' && dist[newX][newY] == -1) {
                dist[newX][newY] = dist[x][y] + 1;
                q.push({newX, newY});
            }
        }
    }
}

void traseu(int finishX, int finishY, int startX, int startY) {
    int x = finishX, y = finishY;

    while (!(x == startX && y == startY)) {
        maze[x][y] = '.';

        for (int k = 0; k < 4; k++) {
            int newX = x + dx[k];
            int newY = y + dy[k];

            if (newX >= 0 && newX < maze.size() && newY >= 0 && newY < maze[0].size() &&
                dist[newX][newY] == dist[x][y] - 1) {
                x = newX;
                y = newY;
                break;
            }
        }
    }
}

int main() {
    int startX = -1, startY = -1, finishX = -1, finishY = -1;

    if (!fin) {
        cout << "Eroare la deschiderea fișierului!" << endl;
        return 1;
    }

    string line;
    while (getline(fin, line)) {
        maze.push_back(vector<char>(line.begin(), line.end()));
    }
    fin.close();

    int rows = maze.size();
    int cols = maze[0].size();

    dist.assign(rows, vector<int>(cols, -1));

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (maze[i][j] == 'S') {
                startX = i;
                startY = j;
            }
            if (maze[i][j] == 'F') {
                finishX = i;
                finishY = j;
            }
        }
    }

    leeBFS(startX, startY, rows, cols);

    if (dist[finishX][finishY] == -1) {
        cout << "Nu există drum de la S la F!" << endl;
        return 1;
    }

    traseu(finishX, finishY, startX, startY);

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            cout << maze[i][j];
        }
        cout << endl;
    }

    return 0;
}