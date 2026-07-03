#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char** argv) {
    FILE* ftin;
    int fbout;
    int rc, cc, i, j;
    int** m;

    ftin = fopen("/home/examiner/rares/2024/os/ro/lab-01/m.txt", "r");
    if(ftin == NULL) {
        perror("Could not open ftin");
        return 1;
    }
    fscanf(ftin, "%d %d", &rc, &cc);

    m = (int**)malloc(rc*sizeof(int*));
    for(i=0; i<rc; i++) {
        m[i] = (int*)malloc(cc*sizeof(int));
        for(j=0; j<cc; j++) {
            fscanf(ftin, "%d", &m[i][j]);
        }
    }
    fclose(ftin);

    for(i=0; i<rc; i++) {
        for(j=0; j<cc; j++) {
            printf("%3d ", m[i][j]);
        }
        printf("\n");
    }

    fbout = open("/home/examiner/rares/2024/os/ro/lab-01/m.bin", O_CREAT | O_TRUNC | O_WRONLY, 0600);
    write(fbout, &rc, sizeof(int));
    write(fbout, &cc, sizeof(int));
    for(i=0; i<rc; i++) {
        for(j=0; j<cc; j++) {
            write(fbout, &m[i][j], sizeof(int));
        }
    }
    close(fbout);

    for(i=0; i<rc; i++) {
        free(m[i]);
    }
    free(m);

    return 0;
}
