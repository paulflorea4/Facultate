#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>

#define BEES 100
#define BEARS 5
#define HONEY_THRESHOLD 30
#define PORTION 10
#define HONEY_FROM_RANGER 20

int honey = 0;

pthread_mutex_t m;
pthread_cond_t c;
pthread_barrier_t b;
sem_t s;

void* bee(void* a) {
    int k = 0;
    while(1) {
        pthread_mutex_lock(&m);
        honey++;
        pthread_mutex_unlock(&m);

        printf("+");
        k++;
        if(k == HONEY_THRESHOLD) {
            printf("#");
            pthread_barrier_wait(&b);
        }
    }
    (void)a;
    return NULL;
}

void* bear(void* a) {
    pthread_barrier_wait(&b);
    while(1) {
        sem_wait(&s);
        pthread_mutex_lock(&m);
        if(honey <= 0) {
            printf("@");
            pthread_cond_signal(&c);
            pthread_mutex_unlock(&m);
            continue;
        }
        honey -= PORTION;
        printf("-");
        pthread_mutex_unlock(&m);
        sem_post(&s);
    }
    (void)a;
    return NULL;
}

void* ranger(void* a) {
    while(1) {
        pthread_mutex_lock(&m);
        while(honey > 0) {
            printf("^");
            pthread_cond_wait(&c, &m);
        }
        printf("$");
        honey += HONEY_FROM_RANGER;
        pthread_mutex_unlock(&m);
    }
    (void)a;
    return NULL;
}

int main() {
    int i;
    pthread_t bees[BEES];
    pthread_t bears[BEARS];
    pthread_t boss;

    pthread_mutex_init(&m, NULL);
    pthread_cond_init(&c, NULL);
    pthread_barrier_init(&b, NULL, BEES+BEARS);
    sem_init(&s, 0, 3);

    for(i=0; i<BEES; i++) {
        pthread_create(&bees[i], NULL, bee, NULL);
    }
    for(i=0; i<BEARS; i++) {
        pthread_create(&bears[i], NULL, bear, NULL);
    }
    pthread_create(&boss, NULL, ranger, NULL);

    for(i=0; i<BEES; i++) {
        pthread_join(bees[i], NULL);
    }
    for(i=0; i<BEARS; i++) {
        pthread_join(bears[i], NULL);
    }
    pthread_join(boss, NULL);

    pthread_mutex_destroy(&m);
    pthread_cond_destroy(&c);
    pthread_barrier_destroy(&b);
    sem_destroy(&s);
    return 0;
}


