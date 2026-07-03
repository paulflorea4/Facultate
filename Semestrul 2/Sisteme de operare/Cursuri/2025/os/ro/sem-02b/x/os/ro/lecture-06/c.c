#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv) {
    int p2g[2], g2w[2];

    pipe(p2g); pipe(g2w);

    if(fork() == 0) {
        close(p2g[0]); close(g2w[0]); close(g2w[1]);
        dup2(p2g[1], 1);
        execlp("ps", "ps", "-ef", NULL);
        exit(0);
    }
    if(fork() == 0) {
        close(p2g[1]); close(g2w[0]);
        dup2(p2g[0], 0);
        dup2(g2w[1], 1);
        execlp("grep", "grep", "-E", "vi", NULL);
        exit(0);
    }
    if(fork() == 0) {
        close(p2g[0]); close(p2g[1]); close(g2w[1]);
        dup2(g2w[0], 0);
        execlp("wc", "wc", "-l", NULL);
        exit(0);
    }
    //close(p2g[0]); close(p2g[1]); close(g2w[0]); close(g2w[1]);
    wait(0); wait(0); wait(0);

    return 0;
}
