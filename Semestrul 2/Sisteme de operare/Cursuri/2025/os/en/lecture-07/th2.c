#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void* f(void* a) {
    printf("%ld\n", (long)a);
    return NULL;
}

int main() {
    pthread_t t[10];
    for(int i=0; i<10; i++) {
        pthread_create(&t[i], NULL, f, (void*)(long)i);
    }
    for(int i=0; i<10; i++) {
        pthread_join(t[i], NULL);
    }
    return 0;
}
