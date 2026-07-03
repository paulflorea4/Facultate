#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/select.h>

#define MSG_LEN 1024

int main() {
    int client_fd;
    struct sockaddr_in srv_addr;
    char msg_buf[MSG_LEN];
    fd_set active_fds;

    client_fd = socket(AF_INET, SOCK_STREAM, 0);
    srv_addr.sin_family = AF_INET;
    srv_addr.sin_port = htons(5555);
    inet_pton(AF_INET, "127.0.0.1", &srv_addr.sin_addr);

    connect(client_fd, (struct sockaddr *)&srv_addr, sizeof(srv_addr));
    printf("Conectat la server! Comenzi: /nick, /msg, /join, /say.\n");

    while (1) {
        FD_ZERO(&active_fds);
        FD_SET(client_fd, &active_fds);
        FD_SET(STDIN_FILENO, &active_fds);

        select(client_fd + 1, &active_fds, NULL, NULL, NULL);

        if (FD_ISSET(STDIN_FILENO, &active_fds)) {
            fgets(msg_buf, MSG_LEN, stdin);
            send(client_fd, msg_buf, strlen(msg_buf), 0);
        }

        if (FD_ISSET(client_fd, &active_fds)) {
            int recv_len = recv(client_fd, msg_buf, MSG_LEN - 1, 0);
            if (recv_len <= 0) {
                printf("Conexiune închisă de server.\n");
                break;
            }
            msg_buf[recv_len] = '\0';
            printf("%s", msg_buf);
        }
    }

    close(client_fd);
    return 0;
}

