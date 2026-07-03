#include <stdio.h>
#include <pthread.h>

int arr[16] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
pthread_barrier_t b;

void* f(void* a) {
    int id = (int)(long)a, k = 2, n = 8;
    while(n > 0) {
        if(id < n) {
            arr[k*id] += arr[k*id + k/2];
        }
        pthread_barrier_wait(&b);
        k *= 2;
        n /= 2;
    }
    return NULL;
}

int main() {
    int i;
    pthread_t t[8];
    pthread_barrier_init(&b, NULL, 8);
    for(i=0; i<8; i++) {
        pthread_create(&t[i], NULL, f, (void*)(long)i);
    }
    for(i=0; i<8; i++) {
        pthread_join(t[i], NULL);
    }
    pthread_barrier_destroy(&b);
    printf("%d\n", arr[0]);
    return 0;
}
