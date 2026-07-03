#include <stdio.h>

int main() {
    int k;
    char s[128];

    FILE* f = popen("who | awk '{print $1}' | sort | uniq -c | sort -n", "r");
    while(1) {
        if(fscanf(f, "%d %s", &k, s) != 2) {
            break;
        }

        printf("Am citit %d sesiuni ale lui %s\n", k, s);
    }

    return 0;
}
