#include<stdio.h>
#include<pthread.h>
#include<semaphore.h>

#define URSI 5
#define ALBINE 30

int miere = 30;
pthread_mutex_t m;
pthread_cond_t c;
pthread_barrier_t b;
sem_t s;

void* albina(void* a) {
    pthread_barrier_wait(&b); while(1) {
        pthread_mutex_lock(&m); printf("+"); miere++; pthread_mutex_unlock(&m);
    } return NULL;
}
void* urs(void* a) {
    pthread_barrier_wait(&b); while(1) {
        sem_wait(&s); pthread_mutex_lock(&m);
        if(miere >= 10) {
            printf("-"); miere -= 10;
        } else {
            printf("!"); pthread_cond_signal(&c);
        }
        pthread_mutex_unlock(&m); sem_post(&s);
    } return NULL;
}
void* padurar(void* a) {
    pthread_barrier_wait(&b); while(1) {
        pthread_mutex_lock(&m);
        while(miere >= 10) { printf("Z"); pthread_cond_wait(&c, &m); }
        printf("M"); miere += 100;
        pthread_mutex_unlock(&m);
    }
    return NULL;
}

int main(int argc, char** argv) {
    pthread_t ursi[URSI], albine[ALBINE], pad;
    int i;

    pthread_mutex_init(&m, NULL);
    pthread_cond_init(&c, NULL);
    pthread_barrier_init(&b, NULL, URSI+ALBINE+1);
    sem_init(&s, 0, 3);

    for(i=0; i<URSI; i++) {
        pthread_create(&ursi[i], NULL, urs, NULL);
    }
    for(i=0; i<ALBINE; i++) {
        pthread_create(&albine[i], NULL, albina, NULL);
    }
    pthread_create(&pad, NULL, padurar, NULL);

    for(i=0; i<URSI; i++) {
        pthread_join(ursi[i], NULL);
    }
    for(i=0; i<ALBINE; i++) {
        pthread_join(albine[i], NULL);
    }
    pthread_join(pad, NULL);

    pthread_mutex_destroy(&m);
    pthread_cond_destroy(&c);
    pthread_barrier_destroy(&b);
    sem_destroy(&s);
    return 0;
}
