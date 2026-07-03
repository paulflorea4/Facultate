#include <stdio.h>
#include <unistd.h>

int main() {
    printf("inainte\n");
    fork();
    printf("dupa\n");
    return 0;
}
