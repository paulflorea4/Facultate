#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>

pid_t fiu[3];
void f(int sgn) {
    int i;
    printf("Parintele a primit semnalul %d\n", sgn);
    for(i=0; i<3; i++) {
        kill(fiu[i], SIGUSR2);
    }
    exit(0);
}
void g(int sgn) {
    printf("Fiul a primit semnalul %d\n", sgn);
    exit(0);
}
int main() {
    int i;
    signal(SIGUSR1, f);

    for(i=0; i<3; i++) {
        fiu[i] = fork();
        if(fiu[i] == 0) {
            signal(SIGUSR2, g);
            while(1);
            exit(0);
        }
    }
    for(i=0; i<3; i++) {
        wait(0);
    }
    return 0;
}
