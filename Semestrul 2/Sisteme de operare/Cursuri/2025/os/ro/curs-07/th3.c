#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define N 10

int count = 0;
pthread_mutex_t m;

void* f(void* a) {
    for(int i=0; i<(long)a; i++) {
        pthread_mutex_lock(&m);
        count++;
        pthread_mutex_unlock(&m);
    }
    return NULL;
}

int main() {
    pthread_t t[N];
    int n = 100000;

    pthread_mutex_init(&m, NULL);
    for(int i=0; i<N; i++) {
        pthread_create(&t[i], NULL, f, (void*)(long)n);
    }
    for(int i=0; i<N; i++) {
        pthread_join(t[i], NULL);
    }
    pthread_mutex_destroy(&m);
    printf("%d\n", count);
    return 0;
}
