#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>

int main(int argc, char** argv) {
    struct timeval start, end;
    double duration;

    gettimeofday(&start, NULL);
    if(fork() == 0) {
        execvp(argv[1], argv+1);
        exit(0);
    }
    wait(NULL);
    gettimeofday(&end, NULL);

    duration = ((end.tv_sec - start.tv_sec) * 1000.0 + (end.tv_usec - start.tv_usec) / 1000.0) / 1000.0;
    printf("%lf\n", duration);

    (void)argc;
    return 0;
}

