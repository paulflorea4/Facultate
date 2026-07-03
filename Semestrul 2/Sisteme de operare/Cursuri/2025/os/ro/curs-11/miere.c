#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>

int miere = 0;
int nealbine = 100;
pthread_mutex_t m;
pthread_cond_t c;
pthread_cond_t c2;
sem_t s;

void* albina(void* a) {
    int k = 0;
    while(1) {
        pthread_mutex_lock(&m);
        miere++;
        pthread_mutex_unlock(&m);

        k++;
        printf("+");
        if(k == 30) {
            pthread_mutex_lock(&m);
            nealbine--;
            if(nealbine == 0) {
                pthread_cond_broadcast(&c2);
            }
            pthread_mutex_unlock(&m);
            printf("#");
        }
    }
    (void)a; return NULL;
}

void* urs(void* a) {
    pthread_mutex_lock(&m);
    while(nealbine > 0) {
        pthread_cond_wait(&c2, &m);
    }
    pthread_mutex_unlock(&m);

    while(1) {
        sem_wait(&s);
        pthread_mutex_lock(&m);
        if(miere < 50) {
            printf("A");
            pthread_mutex_unlock(&m);
            pthread_cond_signal(&c);
            sem_post(&s);
            continue;
        }
        miere -= 50;
        pthread_mutex_unlock(&m);
        sem_post(&s);
        printf("-");
    }
    (void)a; return NULL;
}

void* padurar(void* a) {
    while(1) {
        pthread_mutex_lock(&m);
        while(miere >= 50) {
            pthread_cond_wait(&c, &m);
        }
        miere += 200;
        pthread_mutex_unlock(&m);
        printf("M");
    }
    (void)a; return NULL;
}

int main() {
    int i;
    pthread_t albine[100];
    pthread_t ursi[5];
    pthread_t sef;

    pthread_mutex_init(&m, NULL);
    pthread_cond_init(&c, NULL);
    pthread_cond_init(&c2, NULL);
    sem_init(&s, 0, 3);

    for(i=0; i<100; i++) {
        pthread_create(&albine[i], NULL, albina, NULL);
    }
    for(i=0; i<5; i++) {
        pthread_create(&ursi[i], NULL, urs, NULL);
    }
    pthread_create(&sef, NULL, padurar, NULL);

    pthread_join(sef, NULL);
    for(i=0; i<5; i++) {
        pthread_join(ursi[i], NULL);
    }
    for(i=0; i<100; i++) {
        pthread_join(albine[i], NULL);
    }

    pthread_mutex_destroy(&m);
    pthread_cond_destroy(&c);
    pthread_cond_destroy(&c2);
    sem_destroy(&s);

    return 0;
}
