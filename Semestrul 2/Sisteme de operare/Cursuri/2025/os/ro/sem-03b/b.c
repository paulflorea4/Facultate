#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>

int main(int argc, char** argv) {
    struct timeval t0, t1;
    int x;

    gettimeofday(&t0, NULL);
    if(fork() == 0) {
        execvp(argv[1], argv+1);
        exit(1);
    }
    wait(&x);
    gettimeofday(&t1, NULL);
    printf("%d %d\n", x, WEXITSTATUS(x));

    printf("%lf\n", ((t1.tv_sec-t0.tv_sec)*1000 + (t1.tv_usec - t0.tv_usec)/1000.0)/1000.0);
    (void)argc;
    return 0;
}
