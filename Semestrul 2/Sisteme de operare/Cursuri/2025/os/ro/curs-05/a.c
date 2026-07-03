#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

int main() {
    printf("a\n");
    if(fork() == 0) {
        if(execlp("echo", "echo", "b", NULL) < 0) {
            exit(1);
        }
    }
    printf("c\n");
    wait(0);
    return 0;
}
