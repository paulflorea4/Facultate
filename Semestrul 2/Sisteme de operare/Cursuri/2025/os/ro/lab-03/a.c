/* RO: Copiati acest fisier intr-un alt fisier cu extensia .c (de
 * exemplu a.c) si rezolvati problemele compilare (erori si warning-uri)
 * si rulare (folositi valgrind), incat programul sa citeasca nume de la
 * intrarea standard, sa le salveze intr-o lista simplu inlantuita si sa
 * afiseze un salut care sa difere daca numele a mai fost introdus.
 *
 * EN: Copy this file to another file having a .c extension (eg a.c) and
 * fix the compilation errors/warnings and the runtime problems (use
 * valgrind), so that it will read names from standard input, save them
 * in a linked list, and display a greeting that should differ if the 
 * name was introduced before.
 */
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

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

    if(head == NULL) {
        return n;
    }

    p = head;
    while(p->next != NULL) {
        p = p->next;
    }
    p->next = n;

    return head;
}

void clear(struct node* head) {
    if(head == NULL) {
        return;
    }
    clear(head->next);
    free(head->name);
    free(head);
}

int known(struct node* head, char* name) {
    struct node* p;

    if(head == NULL) {
        return 0;
    }
    p = head;
    while(p != NULL && strcmp(p->name, name) != 0) {
        p = p->next;
    }
    if(p == NULL) {
        return 0;
    }
    return 1;
}

int main() {
    char name[64];
    struct node* head = NULL;

    while(fgets(name, 64, stdin) != NULL) {
        if(name[strlen(name)-1] == '\n') {
            name[strlen(name)-1] = 0;
        }
        if(known(head, name)) {
            printf("Hello again, %s!\n", name);
        }
        else {
            head = add(head, name);
            printf("Hello %s!\n", name);
        }
    }

    clear(head);
    return 0;
}

