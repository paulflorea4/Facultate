#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#define N 10

void* f(void* a) {
    printf("%d\n", *(int*)a);
    free(a);
    return NULL;
}

int main() {
    pthread_t t[N];
    int i;
    int* a;
    for(i=0; i<N; i++) {
        a = (int*)malloc(sizeof(int));
        *a = i;
        pthread_create(&t[i], NULL, f, a);
    }
    for(i=0; i<N; i++) {
        pthread_join(t[i], NULL);
    }
    return 0;
}
