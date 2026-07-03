#include <stdio.h>
#include <pthread.h>

int turn = 0;
int board[3][3] = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
pthread_mutex_t m;

void show() {
    int i, j;
    for(i=0; i<3; i++) {
        for(j=0; j<3; j++) {
            printf("%3d ", board[i][j]);
        }
        printf("\n");
    }
    printf("\n");
}

void* ju0(void* a) {
    int i, j, full;
    while(1) {
        pthread_mutex_lock(&m);
        if(turn == 0) {
            full = 1;
            for(i=0; i<3; i++) { for(j=0; j<3; j++) { if(board[i][j] == -1) { full = 0; board[i][j] = 0; show(); break; } } if(!full) { break; } }
            turn = 1;
            if(full) { pthread_mutex_unlock(&m); break; }
        }
        pthread_mutex_unlock(&m);
    }
    (void)a;
    return NULL;
}

void* ju1(void* a) {
    int i, j, full;
    while(1) {
        pthread_mutex_lock(&m);
        if(turn == 1) {
            full = 1;
            for(i=0; i<3; i++) { for(j=0; j<3; j++) { if(board[i][j] == -1) { full = 0; board[i][j] = 1; show(); break; } } if(!full) { break; } }
            turn = 0;
            if(full) { pthread_mutex_unlock(&m); break; }
        }
        pthread_mutex_unlock(&m);
    }
    (void)a;
    return NULL;
}

int main() {
    pthread_t t0, t1;
    pthread_mutex_init(&m, NULL);
    pthread_create(&t0, NULL, ju0, NULL);
    pthread_create(&t1, NULL, ju1, NULL);
    pthread_join(t0, NULL);
    pthread_join(t1, NULL);
    pthread_mutex_destroy(&m);
    return 0;
}

