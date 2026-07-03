#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int count = 0;
pthread_mutex_t m;

void* f(void* a) {
    int k = 0;
    for(int i=0; i<*(int*)a; i++) {
        k++;
    }
    pthread_mutex_lock(&m);
    count += k;
    pthread_mutex_unlock(&m);
    return NULL;
}

int main(int argc, char** argv) {
    pthread_t t[10];
    int n = 10;

    if(argc > 1) {
        sscanf(argv[1], "%d", &n);
    }

    pthread_mutex_init(&m, NULL);
    for(int i=0; i<10; i++) {
        pthread_create(&t[i], NULL, f, &n);
    }
    for(int i=0; i<10; i++) {
        pthread_join(t[i], NULL);
    }
    pthread_mutex_destroy(&m);
    printf("%d\n", count);
    return 0;
}
