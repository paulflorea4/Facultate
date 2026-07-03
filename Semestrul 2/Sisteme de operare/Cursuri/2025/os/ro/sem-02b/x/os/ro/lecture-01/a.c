#include <stdio.h>

int main(int argc, char** argv) {
    char s[32];

    while(scanf("%s", s) == 1) {
        printf("Salut %s!\n", s);
    }
    return 0;
}
