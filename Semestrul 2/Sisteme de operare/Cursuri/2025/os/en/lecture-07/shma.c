#include <stdio.h>

#include "shm.h"

int main() {
    int shmid, k = 0;
    struct absp *x;

    shmid = shmget(1234, sizeof(struct absp),
                   IPC_CREAT | 0600);
    if(shmid < 0) {
        perror("failed to create shm");
        return 1;
    }
   
    x = shmat(shmid, 0, 0);
    x->s = 1; x->p = 2;
    while(1) {
        x->a = k++ % 100;
        x->b = k++ % 100;
        printf("%d %d %d %d\n",
                x->a, x->b, x->s, x->p);
        if(x->s == x->p) {
            break;
        }
    }

    shmdt(x);
    shmctl(shmid, IPC_RMID, NULL);
    return 0;
}

