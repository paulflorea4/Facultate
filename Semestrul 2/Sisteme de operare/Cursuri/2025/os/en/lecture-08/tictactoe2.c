#include <stdio.h>
#include <pthread.h>

int board[3][3] = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
pthread_mutex_t m0, m1;

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

void* p0(void* a) {
    int i, j, full;
    while(1) {
        pthread_mutex_lock(&m0);
        full = 1;
        for(i=0; i<3; i++) { for(j=0; j<3; j++) { if(board[i][j] == -1) { full = 0; board[i][j] = 0; break; } } if(!full) { break; } }
        show();
        if(full) {
            pthread_mutex_unlock(&m1);
            break;
        }
        pthread_mutex_unlock(&m1);
    }
    (void)a; return NULL;
}

void* p1(void* a) {
    int i, j, full;
    while(1) {
        pthread_mutex_lock(&m1);
        full = 1;
        for(i=0; i<3; i++) { for(j=0; j<3; j++) { if(board[i][j] == -1) { full = 0; board[i][j] = 1; break; } } if(!full) { break; } }
        show();
        if(full) {
            pthread_mutex_unlock(&m0);
            break;
        }
        pthread_mutex_unlock(&m0);
    }
    (void)a; return NULL;
}

int main() {
    pthread_t t0, t1;

    pthread_mutex_init(&m0, NULL);
    pthread_mutex_init(&m1, NULL);
    pthread_mutex_lock(&m1);
    pthread_create(&t0, NULL, p0, NULL);
    pthread_create(&t1, NULL, p1, NULL);

    pthread_join(t0, NULL);
    pthread_join(t1, NULL);
    pthread_mutex_destroy(&m0);
    pthread_mutex_destroy(&m1);
    return 0;
}
