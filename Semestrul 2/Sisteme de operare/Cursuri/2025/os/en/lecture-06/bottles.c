#include<stdio.h>

int main() {
    int i;

    FILE* f = popen("sort | less", "w");
    for(i=99; i>0; i--) {
        fprintf(f, "%d bottles of beer on the wall\n", i);
        fprintf(f, "%d bottles of beer\n", i);
        fprintf(f, "You take on down and pass it around\n");
        fprintf(f, "%d bottles of beer on the wall\n\n", i-1);
    }
    pclose(f);
    return 0;
}
