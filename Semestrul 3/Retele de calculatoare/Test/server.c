#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/select.h>

#define MAX_USERS 100
#define BUFFER_SIZE 1024
#define NAME_LEN 32
#define ROOM_LEN 32

typedef struct {
    int fd;
    char name[NAME_LEN];
    char room[ROOM_LEN];
} User;

User user_list[MAX_USERS];

void broadcast_all(char *message, int except_fd) {
    for (int i = 0; i < MAX_USERS; i++) {
        if (user_list[i].fd != 0 && user_list[i].fd != except_fd) {
            send(user_list[i].fd, message, strlen(message), 0);
        }
    }
}

void broadcast_room(char *message, char *room_name, int except_fd) {
    for (int i = 0; i < MAX_USERS; i++) {
        if (user_list[i].fd != 0 && strcmp(user_list[i].room, room_name) == 0 && user_list[i].fd != except_fd) {
            send(user_list[i].fd, message, strlen(message), 0);
        }
    }
}

void send_direct(char *target_name, char *message) {
    for (int i = 0; i < MAX_USERS; i++) {
        if (user_list[i].fd != 0 && strcmp(user_list[i].name, target_name) == 0) {
            send(user_list[i].fd, message, strlen(message), 0);
            return;
        }
    }
}

int main() {
    int srv_fd, cli_fd, port = 5555;
    struct sockaddr_in srv_addr, cli_addr;
    fd_set active_fds;
    socklen_t addr_len;
    char msg_buf[BUFFER_SIZE];

    memset(user_list, 0, sizeof(user_list));

    srv_fd = socket(AF_INET, SOCK_STREAM, 0);
    srv_addr.sin_family = AF_INET;
    srv_addr.sin_addr.s_addr = INADDR_ANY;
    srv_addr.sin_port = htons(port);

    if (bind(srv_fd, (struct sockaddr *)&srv_addr, sizeof(srv_addr)) < 0) {
        printf("Eroare la bind\n");
        return 1;
    }

    listen(srv_fd, 5);

    while (1) {
        FD_ZERO(&active_fds);
        FD_SET(srv_fd, &active_fds);
        int max_fd = srv_fd;

        for (int i = 0; i < MAX_USERS; i++) {
            int fd = user_list[i].fd;
            if (fd > 0) FD_SET(fd, &active_fds);
            if (fd > max_fd) max_fd = fd;
        }

        select(max_fd + 1, &active_fds, NULL, NULL, NULL);

        if (FD_ISSET(srv_fd, &active_fds)) {
            addr_len = sizeof(cli_addr);
            cli_fd = accept(srv_fd, (struct sockaddr *)&cli_addr, &addr_len);
            for (int i = 0; i < MAX_USERS; i++) {
                if (user_list[i].fd == 0) {
                    user_list[i].fd = cli_fd;
                    strcpy(user_list[i].name, "Anonim");
                    strcpy(user_list[i].room, "");
                    break;
                }
            }
            char greet[] = "Bine ai venit! Seteaza nume cu /nick <nume>\n";
            send(cli_fd, greet, strlen(greet), 0);
        }

        for (int i = 0; i < MAX_USERS; i++) {
            int fd = user_list[i].fd;
            if (FD_ISSET(fd, &active_fds)) {
                int recv_bytes = recv(fd, msg_buf, BUFFER_SIZE - 1, 0);
                if (recv_bytes <= 0) {
                    close(fd);
                    user_list[i].fd = 0;
                    continue;
                }
                msg_buf[recv_bytes] = '\0';

                // comenzi
                if (strncmp(msg_buf, "/nick ", 6) == 0) {
                    sscanf(msg_buf + 6, "%s", user_list[i].name);
                    char feedback[64];
                    sprintf(feedback, "Nickname setat: %s\n", user_list[i].name);
                    send(fd, feedback, strlen(feedback), 0);
                } else if (strncmp(msg_buf, "/join ", 6) == 0) {
                    sscanf(msg_buf + 6, "%s", user_list[i].room);
                    char joined[64];
                    sprintf(joined, "Ai intrat pe canalul %s\n", user_list[i].room);
                    send(fd, joined, strlen(joined), 0);
                } else if (strncmp(msg_buf, "/msg ", 5) == 0) {
                    char target[NAME_LEN], text[BUFFER_SIZE];
                    sscanf(msg_buf + 5, "%s %[^\n]", target, text);
                    char pm[BUFFER_SIZE + NAME_LEN + 16];
                    snprintf(pm, sizeof(pm), "[PM de la %s]: %s\n", user_list[i].name, text);
                    send_direct(target, pm);
                } else if (strncmp(msg_buf, "/say ", 5) == 0) {
                    char text[BUFFER_SIZE];
                    sscanf(msg_buf + 5, "%[^\n]", text);
                    char chat[BUFFER_SIZE + NAME_LEN + ROOM_LEN + 8];
                    snprintf(chat, sizeof(chat), "[%s @ %s]: %s\n", user_list[i].name, user_list[i].room, text);
                    broadcast_room(chat, user_list[i].room, fd);
                } else {
                    // broadcast global
                    char general[BUFFER_SIZE + NAME_LEN + 4];
                    snprintf(general, sizeof(general), "[%s]: %s", user_list[i].name, msg_buf);
                    broadcast_all(general, fd);
                }
            }
        }
    }
}

