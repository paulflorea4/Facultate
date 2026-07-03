#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

int main() {
    int i;

    for(i=0; i<3; i++) {
        if(fork() == 0) {
            printf("%d %d %d\n", getpid(), getppid(), i);
            exit(0);
        }
    }
    for(i=0; i<3; i++) {
        wait(NULL);
    }
    return 0;
}
