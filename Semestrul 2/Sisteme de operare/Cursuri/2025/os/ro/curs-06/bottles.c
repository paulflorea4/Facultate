#include <stdio.h>

int main() {
    int i;

    FILE* f = popen("sort -n| grep -E '.'|less", "w");
    for(i=99; i>0; i--) {
        fprintf(f, "%d bottles of beer on the wall, %d bottles of beer\n", i, i);
        fprintf(f, "You take one down and pass it around, %d bottles of beer on the wall\n\n", i-1);
    }
    pclose(f);
    return 0;
}
