#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>

pid_t pids[3];

void fp(int sgn) {
    if(sgn == SIGUSR1) {
        for(int i=0; i<3; i++) {
            kill(pids[i], SIGUSR2);
        }
    } else if(sgn == SIGUSR2) {
        for(int i=0; i<3; i++) {
            kill(pids[i], SIGKILL);
        }
    }
}
void fc(int sgn) {
    printf("%d %d\n", getpid(), sgn);
}
int main() {
    for(int i=0; i<3; i++) {
        pids[i] = fork();
        if(pids[i] == 0) {
            signal(SIGUSR2, fc);
            while(1);
            exit(0);
        }
    }

    signal(SIGUSR1, fp);
    signal(SIGUSR2, fp);

    for(int i=0; i<3; i++) { wait(0); }
    return 0;
}
