#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char** argv) {
    int f, k, i;

    f = open(argv[1], O_RDWR);
    if(f < 0) {
        perror("failed to open file");
        return 1;
    }
    if(argc > 2 && strcmp(argv[2], "reset") == 0) {
        k = 0;
        write(f, &k, sizeof(int));
        close(f);
        return 0;
    }

    for(i=0; i<128; i++) {
        lseek(f, 0, SEEK_SET);
        read(f, &k, sizeof(int));
        k++;
        lseek(f, 0, SEEK_SET);
        write(f, &k, sizeof(int));
    }
    close(f);
    return 0;
}
