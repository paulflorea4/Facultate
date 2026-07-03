#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv) {
    if(fork() == 0) {
        if(execlp("cat", "dog", NULL) == -1) {
            perror("something went bad");
            exit(0);
        }
    }
    printf("we echoed!\n");
    wait(0);
    return 0;
}
