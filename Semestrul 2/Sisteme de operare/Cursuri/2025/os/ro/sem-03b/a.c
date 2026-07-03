#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <string.h>

void f(int sgn) {
    char s[8];
    printf("Chiar sa ma opresc? ");
    scanf("%s", s);
    if(strcmp(s, "da") == 0) {
        exit(0);
    }
    (void)sgn;
}

int main() {
    int i = 0;
    signal(SIGINT, f);
    while(1) {
        printf("%d\n", i);
        i++;
        sleep(1);
    }
    return 0;
}
