#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct node {
    char* name;
    struct node* next;
};

struct node* add(struct node* head, char* name) {
    struct node* n;
    struct node* p;

    n = (struct node*)malloc(sizeof(struct node));
    n->name = (char*)malloc(strlen(name)+1);
    strcpy(n->name, name);
    n->next = NULL;

    if(head== NULL) {
        return n;
    }

    p = head;
    while(p->next != NULL) {
        p = p->next;
    }
    p->next = n;

    return head;
}

int main(int argc, char** argv) {
    char name[32];
    struct node* head = NULL;

    while(scanf("%s", name) == 1) {
        printf("Hello %s\n", name);
        add(head, name);   
    }
    return 0;
}
