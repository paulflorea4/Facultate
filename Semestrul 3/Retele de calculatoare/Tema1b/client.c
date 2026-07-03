#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <stdlib.h>

int main(int argc,char** argv){
        int c;
        struct sockaddr_in server;
        char s[100];
        uint16_t l,i;

        c=socket(AF_INET,SOCK_STREAM,0);
        if(c<0){
                printf("Eroare la crearea socketului client\n");
                return 1;
        }

	if(argc<3){
		printf("Introduceti portul si adresa ip a serverului\n");
		return 1;
	}

        memset(&server,0,sizeof(server));
        server.sin_port=htons(atoi(argv[1]));
        server.sin_family=AF_INET;
        server.sin_addr.s_addr=inet_addr(argv[2]);

        if(connect(c,(struct sockaddr *) &server,sizeof(server))<0){
                printf("Eroare la conectarea la server\n");
                return 1;
	}

	printf("l = ");
        scanf("%hu",&l);

        printf("i = ");
        scanf("%hu",&i);

        printf("Introduceti un sir:\n");
        getchar();
        fgets(s,100,stdin);

        l=htons(l);
        i=htons(i);
	uint16_t len=strlen(s);
	len=htons(len);

	send(c,&len,sizeof(len),0);
        send(c,s,strlen(s),0);
        send(c,&l,sizeof(l),0);
        send(c,&i,sizeof(i),0);

        char r[100];
        int n=recv(c,r,l,MSG_WAITALL);
        if(n>0){
                printf(r);
                printf("\n");

        }
        close(c);
}
