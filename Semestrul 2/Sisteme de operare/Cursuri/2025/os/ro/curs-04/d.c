#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    if(fork() == 0) {
        sleep(10);
        exit(0);
    }
    sleep(15);
    wait(0);
    sleep(5);
    return 0;
}
