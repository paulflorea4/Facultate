#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv) {
    int i;

    for(i=0; i<3; i++) {
        if(fork() == 0) {
            sleep(5);
            exit(0);
        }
    }
    sleep(15);

    for(i=0; i<3; i++) {
        wait(0);
    }
    return 0;
}
