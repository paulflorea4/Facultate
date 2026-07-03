#include <stdio.h>
#include <unistd.h>

int main() {
    printf("before\n");
    fork();
    printf("after\n");
    return 0;
}
