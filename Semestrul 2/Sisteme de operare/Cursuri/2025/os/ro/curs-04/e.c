#include <stdio.h>
#include <signal.h>

void f(int sgn) {
    printf("Ha ha, ma incearca\n");
    (void)sgn;
}

int main() {
    signal(SIGINT, f);
    while(1);
    return 0;
}
