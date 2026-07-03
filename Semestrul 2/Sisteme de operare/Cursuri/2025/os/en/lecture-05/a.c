#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

int main() {
    printf("asdf\n");
    if(fork() == 0) {
        if(execlp("echo", "echo", "hellow world", NULL) < 0) {
            exit(1);
        }
    }
    sleep(10);
    printf("qwer\n");
    wait(0);
    return 0;
}
