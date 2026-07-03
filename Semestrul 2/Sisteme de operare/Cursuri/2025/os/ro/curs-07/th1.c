#include <stdio.h>
#include <pthread.h>

struct arg {
    int n;
    char* s;
};

void* f(void* a) {
    struct arg* p = (struct arg*)a;
    for(int i=0; i<p->n; i++) {
        printf("%s\n", p->s);
    }
    (void)a;
    return NULL;
}

int main() {
    pthread_t t;
    struct arg a;
    a.n = 100000;
    a.s = "threaduletz";
    pthread_create(&t, NULL, f, &a);
    for(int i=0; i<a.n; i++) {
        printf("mainuletz\n");
    }
    pthread_join(t, NULL);
    return 0;
}
