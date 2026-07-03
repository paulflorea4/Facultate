#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <fcntl.h>

int main() {
    int p2g[2], g2a[2], a2t[2];

    pipe(p2g); pipe(g2a); pipe(a2t);

    if(fork() == 0) { // ps
        close(p2g[0]); close(g2a[0]); close(g2a[1]); close(a2t[0]); close(a2t[1]);
        dup2(p2g[1], 1);
        execlp("ps", "ps", "-ef", NULL);
        exit(1);
    }
    if(fork() == 0) { // grep
        close(p2g[1]); close(g2a[0]); close(a2t[0]); close(a2t[1]);
        dup2(p2g[0], 0); dup2(g2a[1], 1);
        execlp("grep", "grep", "-E", "^root\\>", NULL);
        exit(1);
    }
    if(fork() == 0) { // awk
        close(p2g[0]); close(p2g[1]); close(g2a[1]); close(a2t[0]);
        dup2(g2a[0], 0); dup2(a2t[1], 1);
        execlp("awk", "awk", "{print $2}", NULL);
        exit(1);
    }
    if(fork() == 0) { // tail
        close(p2g[0]); close(p2g[1]); close(g2a[0]); close(g2a[1]); close(a2t[1]);
        dup2(a2t[0], 0);
        execlp("tail", "tail", "-n", "5", NULL);
        exit(1);
    }

    close(p2g[0]); close(p2g[1]);
    close(g2a[0]); close(g2a[1]);
    close(a2t[0]); close(a2t[1]);
    wait(0); wait(0); wait(0); wait(0);
    return 0;
}
