#include <stdio.h>
#include <pthread.h>

struct arg {
    int n;
    char* msg;
};

void* f(void* a) {
    struct arg* p = (struct arg*)a;

    for(int i=0; i<p->n; i++) {
        printf("%s\n", p->msg);
    }
    return NULL;
}

int main() {
    pthread_t t;
    struct arg a;
    a.n = 100000;
    a.msg = "asdf";
    pthread_create(&t, NULL, f, &a);
    for(int i=0; i<a.n; i++) {
        printf("mainy\n");
    }
    pthread_join(t, NULL);
    return 0;
}
